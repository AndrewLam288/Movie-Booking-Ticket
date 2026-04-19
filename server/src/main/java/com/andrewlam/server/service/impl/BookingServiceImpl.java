package com.andrewlam.server.service.impl;

import com.andrewlam.server.common.exception.BadRequestException;
import com.andrewlam.server.common.exception.ConflictException;
import com.andrewlam.server.common.exception.ResourceNotFoundException;
import com.andrewlam.server.dto.request.CreateBookingRequestDto;
import com.andrewlam.server.dto.response.BookingResponseDto;
import com.andrewlam.server.enums.BookingStatus;
import com.andrewlam.server.mapper.BookingMapper;
import com.andrewlam.server.model.Booking;
import com.andrewlam.server.model.BookingSeat;
import com.andrewlam.server.model.Seat;
import com.andrewlam.server.model.Showtime;
import com.andrewlam.server.model.User;
import com.andrewlam.server.repository.BookingRepository;
import com.andrewlam.server.repository.BookingSeatRepository;
import com.andrewlam.server.repository.SeatRepository;
import com.andrewlam.server.repository.ShowtimeRepository;
import com.andrewlam.server.repository.UserRepository;
import com.andrewlam.server.service.BookingService;
import com.andrewlam.server.service.SeatService;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final BookingSeatRepository bookingSeatRepository;
    private final SeatRepository seatRepository;
    private final ShowtimeRepository showtimeRepository;
    private final UserRepository userRepository;
    private final SeatService seatService;
    private final BookingMapper bookingMapper;

    public BookingServiceImpl(
            BookingRepository bookingRepository,
            BookingSeatRepository bookingSeatRepository,
            SeatRepository seatRepository,
            ShowtimeRepository showtimeRepository,
            UserRepository userRepository,
            SeatService seatService,
            BookingMapper bookingMapper
    ) {
        this.bookingRepository = bookingRepository;
        this.bookingSeatRepository = bookingSeatRepository;
        this.seatRepository = seatRepository;
        this.showtimeRepository = showtimeRepository;
        this.userRepository = userRepository;
        this.seatService = seatService;
        this.bookingMapper = bookingMapper;
    }

    @Override
    @Transactional
    public BookingResponseDto createBooking(CreateBookingRequestDto requestDto, String principalName) {
        try {
            User user = resolveAuthenticatedUser(principalName);
            String holderKey = buildHolderKey(user.getId());

            Set<Long> uniqueSeatIds = normalizeSeatIds(requestDto.getSeatIds());

            Showtime showtime = showtimeRepository.findById(requestDto.getShowtimeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Showtime not found."));

            List<Long> alreadyBookedSeatIds = bookingSeatRepository.findBookedSeatIds(showtime.getId(), uniqueSeatIds);
            if (!alreadyBookedSeatIds.isEmpty()) {
                throw new ConflictException("Some selected seats are already booked.");
            }

            Set<Long> validHeldSeatIds = seatService.getValidHeldSeatIds(showtime.getId(), uniqueSeatIds, holderKey);
            if (!validHeldSeatIds.containsAll(uniqueSeatIds)) {
                throw new ConflictException("Some selected seats are not actively held by this user or have expired.");
            }

            List<Seat> seats = seatRepository.findAllById(uniqueSeatIds);
            if (seats.size() != uniqueSeatIds.size()) {
                throw new BadRequestException("One or more selected seats do not exist.");
            }

            validateSeatsBelongToShowtimeRoom(showtime, seats);

            Booking booking = new Booking();
            booking.setBookingCode(generateBookingCode());
            booking.setUser(user);
            booking.setShowtime(showtime);
            booking.setStatus(BookingStatus.CONFIRMED);
            booking.setBookedAt(OffsetDateTime.now());

            BigDecimal totalAmount = BigDecimal.ZERO;
            List<BookingSeat> bookingSeats = new ArrayList<>();

            for (Seat seat : seats) {
                BigDecimal seatPrice = calculateSeatPrice(showtime, seat);
                totalAmount = totalAmount.add(seatPrice);

                BookingSeat bookingSeat = new BookingSeat();
                bookingSeat.setBooking(booking);
                bookingSeat.setShowtime(showtime);
                bookingSeat.setSeat(seat);
                bookingSeat.setPrice(seatPrice);

                bookingSeats.add(bookingSeat);
            }

            booking.setTotalAmount(totalAmount);
            booking.setBookingSeats(bookingSeats);

            Booking savedBooking = bookingRepository.save(booking);

            seatService.consumeHeldSeatsAfterBooking(showtime.getId(), uniqueSeatIds, holderKey);
            seatService.publishBookedSeats(showtime.getId(), uniqueSeatIds);

            return bookingMapper.toResponseDto(savedBooking);

        } catch (DataIntegrityViolationException ex) {
            throw new ConflictException("One or more seats were booked by another request. Please refresh and try again.");
        }
    }

    @Override
    @Transactional
    public BookingResponseDto getMyBooking(String bookingCode, String principalName) {
        User user = resolveAuthenticatedUser(principalName);

        Booking booking = bookingRepository.findMyBookingByCode(bookingCode, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found."));

        booking.getBookingSeats().size();
        return bookingMapper.toResponseDto(booking);
    }

    private User resolveAuthenticatedUser(String principalName) {
        return userRepository.findByEmailIgnoreCase(principalName)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authenticated user not found."));
    }

    private String buildHolderKey(Long userId) {
        return "USER:" + userId;
    }

    private Set<Long> normalizeSeatIds(List<Long> seatIds) {
        if (seatIds == null || seatIds.isEmpty()) {
            throw new BadRequestException("At least one seat must be selected.");
        }

        return seatIds.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private void validateSeatsBelongToShowtimeRoom(Showtime showtime, List<Seat> seats) {
        for (Seat seat : seats) {
            if (seat.getRoom() == null || !seat.getRoom().getId().equals(showtime.getRoom().getId())) {
                throw new BadRequestException("One or more selected seats do not belong to this showtime room.");
            }

            if (Boolean.FALSE.equals(seat.getIsActive())) {
                throw new BadRequestException("One or more selected seats are inactive.");
            }
        }
    }

    private BigDecimal calculateSeatPrice(Showtime showtime, Seat seat) {
        return showtime.getBasePrice();
    }

    private String generateBookingCode() {
        return "BK" + UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 12)
                .toUpperCase();
    }
}