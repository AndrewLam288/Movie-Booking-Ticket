package com.andrewlam.server.repository;

import com.andrewlam.server.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Responsible for data access for Movie entity.
// Provides basic CRUD operations through JpaRepository.
// Can be extended with custom query methods such as
// finding movies by title, status, cinema, or release date.
public interface MovieRepository extends JpaRepository<Movie, Long> {

    // Take all movies with order follow releaseDate
    List<Movie> findAllByOrderByReleaseDateDesc();
}