package com.andrewlam.server.repository;

import com.andrewlam.server.model.SeatHold;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface SeatHoldRepository extends JpaRepository<SeatHold, Long> {

    List<SeatHold> findByShowtimeId(Long showtimeId);

    Optional<SeatHold> findByShowtimeIdAndSeatId(Long showtimeId, Long seatId);

    Optional<SeatHold> findByShowtimeIdAndSeatIdAndClientSessionId(Long showtimeId, Long seatId, String clientSessionId);

    List<SeatHold> findByExpiresAtBefore(OffsetDateTime now);
}