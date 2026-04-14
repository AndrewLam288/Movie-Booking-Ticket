package com.andrewlam.server.repository;

import com.andrewlam.server.model.Cinema;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CinemaRepository extends JpaRepository<Cinema, Long> {
    List<Cinema> findAllByIsActiveTrueOrderByNameAsc();

    Optional<Cinema> findByIdAndIsActiveTrue(Long id);
}