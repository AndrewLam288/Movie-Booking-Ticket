package com.andrewlam.server.repository;

import com.andrewlam.server.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long> {

    List<Seat> findByRoomIdOrderBySeatRowAscSeatNumberAsc(Long roomId);
}