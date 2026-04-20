package com.andrewlam.server.repository;

import com.andrewlam.server.model.Showtime;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {

    @EntityGraph(attributePaths = {"movie", "room", "room.cinema"})
    List<Showtime> findAllByRoomCinemaIdOrderByStartTimeAsc(Long cinemaId);

    @EntityGraph(attributePaths = {"movie", "room", "room.cinema"})
    List<Showtime> findAllByMovieIdOrderByStartTimeAsc(Long movieId);

    @Override
    @EntityGraph(attributePaths = {"movie", "room", "room.cinema"})
    Optional<Showtime> findById(Long id);
}