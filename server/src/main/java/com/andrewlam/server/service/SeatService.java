package com.andrewlam.server.service;

import com.andrewlam.server.dto.response.SeatMapResponseDto;

import java.util.Collection;
import java.util.Set;

public interface SeatService {

    SeatMapResponseDto getSeatMap(Long showtimeId);

    void holdSeat(Long showtimeId, Long seatId, String holderKey);

    void releaseSeat(Long showtimeId, Long seatId, String holderKey);

    void cleanupExpiredSeatHolds();

    Set<Long> getValidHeldSeatIds(Long showtimeId, Collection<Long> seatIds, String holderKey);

    void consumeHeldSeatsAfterBooking(Long showtimeId, Collection<Long> seatIds, String holderKey);

    void publishBookedSeats(Long showtimeId, Collection<Long> seatIds);
}