package com.andrewlam.server.service.impl;

import com.andrewlam.server.common.exception.BadRequestException;
import com.andrewlam.server.common.exception.ConflictException;
import com.andrewlam.server.common.exception.ResourceNotFoundException;
import com.andrewlam.server.dto.SeatAvailabilityEventDto;
import com.andrewlam.server.dto.response.SeatItemResponseDto;
import com.andrewlam.server.dto.response.SeatMapResponseDto;
import com.andrewlam.server.enums.SeatAvailabilityStatus;
import com.andrewlam.server.model.Room;
import com.andrewlam.server.model.Seat;
import com.andrewlam.server.model.SeatHold;
import com.andrewlam.server.model.Showtime;
import com.andrewlam.server.repository.SeatHoldRepository;
import com.andrewlam.server.repository.SeatRepository;
import com.andrewlam.server.repository.ShowtimeRepository;
import com.andrewlam.server.service.SeatService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class SeatServiceImpl implements SeatService {

    private final SeatRepository seatRepository;
    private final SeatHoldRepository seatHoldRepository;
    private final ShowtimeRepository showtimeRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Value("${app.seat-hold.duration-minutes:5}")
    private long seatHoldDurationMinutes;

    public SeatServiceImpl(
            SeatRepository seatRepository,
            SeatHoldRepository seatHoldRepository,
            ShowtimeRepository showtimeRepository,
            SimpMessagingTemplate messagingTemplate
    ) {
        this.seatRepository = seatRepository;
        this.seatHoldRepository = seatHoldRepository;
        this.showtimeRepository = showtimeRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    @Transactional
    public SeatMapResponseDto getSeatMap(Long showtimeId) {
        Showtime showtime = showtimeRepository.findById(showtimeId)
                .orElseThrow(() -> new ResourceNotFoundException("Showtime not found with id: " + showtimeId));

        Room room = showtime.getRoom();

        List<Seat> seats = seatRepository.findByRoomIdOrderBySeatRowAscSeatNumberAsc(room.getId());

        OffsetDateTime now = OffsetDateTime.now();

        List<SeatHold> activeHolds = seatHoldRepository.findByShowtimeId(showtimeId)
                .stream()
                .filter(hold -> hold.getExpiresAt().isAfter(now))
                .toList();

        Map<Long, SeatHold> holdBySeatId = new HashMap<>();
        for (SeatHold hold : activeHolds) {
            holdBySeatId.put(hold.getSeat().getId(), hold);
        }

        List<SeatItemResponseDto> seatItems = seats.stream()
                .map(seat -> mapToSeatItemResponseDto(seat, holdBySeatId.get(seat.getId())))
                .toList();

        SeatMapResponseDto response = new SeatMapResponseDto();
        response.setShowtimeId(showtimeId);
        response.setRoomId(room.getId());
        response.setRoomName(room.getName());
        response.setSeats(seatItems);

        return response;
    }

    @Override
    @Transactional
    public void holdSeat(Long showtimeId, Long seatId, String clientSessionId) {
        Showtime showtime = showtimeRepository.findById(showtimeId)
                .orElseThrow(() -> new ResourceNotFoundException("Showtime not found with id: " + showtimeId));

        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new ResourceNotFoundException("Seat not found with id: " + seatId));

        validateSeatBelongsToShowtimeRoom(showtime, seat);

        if (Boolean.FALSE.equals(seat.getIsActive())) {
            throw new BadRequestException("Seat is inactive and cannot be selected");
        }

        OffsetDateTime now = OffsetDateTime.now();

        Optional<SeatHold> existingHoldOptional = seatHoldRepository.findByShowtimeIdAndSeatId(showtimeId, seatId);

        if (existingHoldOptional.isPresent()) {
            SeatHold existingHold = existingHoldOptional.get();

            if (existingHold.getExpiresAt().isAfter(now)
                    && !existingHold.getClientSessionId().equals(clientSessionId)) {
                throw new ConflictException("Seat is already held by another session");
            }
        }

        SeatHold hold = seatHoldRepository
                .findByShowtimeIdAndSeatIdAndClientSessionId(showtimeId, seatId, clientSessionId)
                .orElseGet(SeatHold::new);

        hold.setShowtime(showtime);
        hold.setSeat(seat);
        hold.setClientSessionId(clientSessionId);
        hold.setExpiresAt(now.plusMinutes(seatHoldDurationMinutes));

        seatHoldRepository.save(hold);

        publishSeatAvailabilityEvent(showtimeId, seatId, SeatAvailabilityStatus.HELD, clientSessionId);
    }

    @Override
    @Transactional
    public void releaseSeat(Long showtimeId, Long seatId, String clientSessionId) {
        SeatHold hold = seatHoldRepository
                .findByShowtimeIdAndSeatIdAndClientSessionId(showtimeId, seatId, clientSessionId)
                .orElseThrow(() -> new ResourceNotFoundException("No active hold found for this seat and session"));

        seatHoldRepository.delete(hold);

        publishSeatAvailabilityEvent(showtimeId, seatId, SeatAvailabilityStatus.AVAILABLE, clientSessionId);
    }

    @Override
    @Transactional
    @Scheduled(fixedDelayString = "${app.seat-hold.cleanup-fixed-delay-ms:30000}")
    public void cleanupExpiredSeatHolds() {
        OffsetDateTime now = OffsetDateTime.now();

        List<SeatHold> expiredHolds = seatHoldRepository.findByExpiresAtBefore(now);

        for (SeatHold expiredHold : expiredHolds) {
            Long showtimeId = expiredHold.getShowtime().getId();
            Long seatId = expiredHold.getSeat().getId();
            String clientSessionId = expiredHold.getClientSessionId();

            seatHoldRepository.delete(expiredHold);

            publishSeatAvailabilityEvent(
                    showtimeId,
                    seatId,
                    SeatAvailabilityStatus.AVAILABLE,
                    clientSessionId
            );
        }
    }

    private void validateSeatBelongsToShowtimeRoom(Showtime showtime, Seat seat) {
        if (!showtime.getRoom().getId().equals(seat.getRoom().getId())) {
            throw new BadRequestException("Seat does not belong to the showtime room");
        }
    }

    private SeatItemResponseDto mapToSeatItemResponseDto(Seat seat, SeatHold hold) {
        SeatItemResponseDto response = new SeatItemResponseDto();
        response.setSeatId(seat.getId());
        response.setSeatRow(seat.getSeatRow());
        response.setSeatNumber(seat.getSeatNumber());
        response.setSeatType(seat.getSeatType());
        response.setIsActive(seat.getIsActive());

        if (Boolean.FALSE.equals(seat.getIsActive())) {
            response.setStatus(SeatAvailabilityStatus.UNAVAILABLE);
            response.setHeldBySessionId(null);
            return response;
        }

        if (hold != null) {
            response.setStatus(SeatAvailabilityStatus.HELD);
            response.setHeldBySessionId(hold.getClientSessionId());
            return response;
        }

        response.setStatus(SeatAvailabilityStatus.AVAILABLE);
        response.setHeldBySessionId(null);
        return response;
    }

    private void publishSeatAvailabilityEvent(
            Long showtimeId,
            Long seatId,
            SeatAvailabilityStatus status,
            String clientSessionId
    ) {
        SeatAvailabilityEventDto event = new SeatAvailabilityEventDto();
        event.setShowtimeId(showtimeId);
        event.setSeatId(seatId);
        event.setStatus(status);
        event.setClientSessionId(clientSessionId);

        messagingTemplate.convertAndSend(
                "/topic/showtimes/" + showtimeId + "/seats",
                event
        );
    }
}