package com.andrewlam.server.service;

import com.andrewlam.server.dto.response.SeatMapResponseDto;

public interface SeatService {

    SeatMapResponseDto getSeatMap(Long showtimeId);

    void holdSeat(Long showtimeId, Long seatId, String clientSessionId);

    void releaseSeat(Long showtimeId, Long seatId, String clientSessionId);

    void cleanupExpiredSeatHolds();
}