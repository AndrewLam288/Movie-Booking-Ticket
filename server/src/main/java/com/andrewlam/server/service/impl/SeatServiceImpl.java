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
import com.andrewlam.server.repository.BookingSeatRepository;
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
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SeatServiceImpl implements SeatService {

    private final SeatRepository seatRepository;
    private final SeatHoldRepository seatHoldRepository;
    private final ShowtimeRepository showtimeRepository;
    private final BookingSeatRepository bookingSeatRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Value("${app.seat-hold.duration-minutes}")
    private long seatHoldDurationMinutes;

    public SeatServiceImpl(
            SeatRepository seatRepository,
            SeatHoldRepository seatHoldRepository,
            ShowtimeRepository showtimeRepository,
            BookingSeatRepository bookingSeatRepository,
            SimpMessagingTemplate messagingTemplate
    ) {
        this.seatRepository = seatRepository;
        this.seatHoldRepository = seatHoldRepository;
        this.showtimeRepository = showtimeRepository;
        this.bookingSeatRepository = bookingSeatRepository;
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

        Set<Long> bookedSeatIds = new HashSet<>(bookingSeatRepository.findBookedSeatIdsByShowtimeId(showtimeId));

        List<SeatItemResponseDto> seatItems = seats.stream()
                .map(seat -> mapToSeatItemResponseDto(
                        seat,
                        holdBySeatId.get(seat.getId()),
                        bookedSeatIds.contains(seat.getId())
                ))
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
    public void holdSeat(Long showtimeId, Long seatId, String holderKey) {
        Showtime showtime = showtimeRepository.findById(showtimeId)
                .orElseThrow(() -> new ResourceNotFoundException("Showtime not found with id: " + showtimeId));

        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new ResourceNotFoundException("Seat not found with id: " + seatId));

        validateSeatBelongsToShowtimeRoom(showtime, seat);

        if (Boolean.FALSE.equals(seat.getIsActive())) {
            throw new BadRequestException("Seat is inactive and cannot be selected");
        }

        if (bookingSeatRepository.existsBookedSeat(showtimeId, seatId)) {
            throw new ConflictException("Seat is already booked");
        }

        OffsetDateTime now = OffsetDateTime.now();

        Optional<SeatHold> existingHoldOptional = seatHoldRepository.findByShowtimeIdAndSeatId(showtimeId, seatId);

        if (existingHoldOptional.isPresent()) {
            SeatHold existingHold = existingHoldOptional.get();

            if (existingHold.getExpiresAt().isAfter(now)
                    && !existingHold.getClientSessionId().equals(holderKey)) {
                throw new ConflictException("Seat is already held by another user");
            }

            if (!existingHold.getExpiresAt().isAfter(now)) {
                seatHoldRepository.delete(existingHold);
            }
        }

        SeatHold hold = seatHoldRepository
                .findByShowtimeIdAndSeatIdAndClientSessionId(showtimeId, seatId, holderKey)
                .orElseGet(SeatHold::new);

        hold.setShowtime(showtime);
        hold.setSeat(seat);
        hold.setClientSessionId(holderKey);
        hold.setExpiresAt(now.plusMinutes(seatHoldDurationMinutes));

        seatHoldRepository.save(hold);

        publishSeatAvailabilityEvent(showtimeId, seatId, SeatAvailabilityStatus.HELD, holderKey);
    }

    @Override
    @Transactional
    public void releaseSeat(Long showtimeId, Long seatId, String holderKey) {
        SeatHold hold = seatHoldRepository
                .findByShowtimeIdAndSeatIdAndClientSessionId(showtimeId, seatId, holderKey)
                .orElseThrow(() -> new ResourceNotFoundException("No active hold found for this seat and user"));

        seatHoldRepository.delete(hold);

        publishSeatAvailabilityEvent(showtimeId, seatId, SeatAvailabilityStatus.AVAILABLE, holderKey);
    }

    @Override
    @Transactional
    @Scheduled(fixedDelayString = "${app.seat-hold.cleanup-fixed-delay-ms}")
    public void cleanupExpiredSeatHolds() {
        OffsetDateTime now = OffsetDateTime.now();

        List<SeatHold> expiredHolds = seatHoldRepository.findByExpiresAtBefore(now);

        for (SeatHold expiredHold : expiredHolds) {
            Long showtimeId = expiredHold.getShowtime().getId();
            Long seatId = expiredHold.getSeat().getId();

            seatHoldRepository.delete(expiredHold);

            publishSeatAvailabilityEvent(
                    showtimeId,
                    seatId,
                    SeatAvailabilityStatus.AVAILABLE,
                    null
            );
        }
    }

    @Override
    @Transactional
    public Set<Long> getValidHeldSeatIds(Long showtimeId, Collection<Long> seatIds, String holderKey) {
        if (seatIds == null || seatIds.isEmpty()) {
            return Collections.emptySet();
        }

        OffsetDateTime now = OffsetDateTime.now();

        return seatHoldRepository.findByShowtimeIdAndSeatIdIn(showtimeId, seatIds)
                .stream()
                .filter(hold -> hold.getExpiresAt().isAfter(now))
                .filter(hold -> holderKey.equals(hold.getClientSessionId()))
                .map(hold -> hold.getSeat().getId())
                .collect(Collectors.toSet());
    }

    @Override
    @Transactional
    public void consumeHeldSeatsAfterBooking(Long showtimeId, Collection<Long> seatIds, String holderKey) {
        if (seatIds == null || seatIds.isEmpty()) {
            return;
        }

        List<SeatHold> holds = seatHoldRepository
                .findByShowtimeIdAndSeatIdInAndClientSessionId(showtimeId, seatIds, holderKey);

        if (!holds.isEmpty()) {
            seatHoldRepository.deleteAll(holds);
        }
    }

    @Override
    public void publishBookedSeats(Long showtimeId, Collection<Long> seatIds) {
        if (seatIds == null || seatIds.isEmpty()) {
            return;
        }

        for (Long seatId : seatIds) {
            publishSeatAvailabilityEvent(showtimeId, seatId, SeatAvailabilityStatus.BOOKED, null);
        }
    }

    private void validateSeatBelongsToShowtimeRoom(Showtime showtime, Seat seat) {
        if (!showtime.getRoom().getId().equals(seat.getRoom().getId())) {
            throw new BadRequestException("Seat does not belong to the showtime room");
        }
    }

    private SeatItemResponseDto mapToSeatItemResponseDto(Seat seat, SeatHold hold, boolean booked) {
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

        if (booked) {
            response.setStatus(SeatAvailabilityStatus.BOOKED);
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
            String holderKey
    ) {
        SeatAvailabilityEventDto event = new SeatAvailabilityEventDto();
        event.setShowtimeId(showtimeId);
        event.setSeatId(seatId);
        event.setStatus(status);
        event.setClientSessionId(holderKey);

        messagingTemplate.convertAndSend(
                "/topic/showtimes/" + showtimeId + "/seats",
                event
        );
    }
}