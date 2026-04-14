package com.andrewlam.server.repository;

import com.andrewlam.server.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {

    List<Room> findAllByCinemaIdAndIsActiveTrueOrderByNameAsc(Long cinemaId);
}