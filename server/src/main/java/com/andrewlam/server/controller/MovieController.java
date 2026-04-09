package com.andrewlam.server.controller;

import com.andrewlam.server.dto.response.MovieDetailResponseDto;
import com.andrewlam.server.dto.response.MovieSummaryResponseDto;
import com.andrewlam.server.service.MovieService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/movies")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    public List<MovieSummaryResponseDto> getAllMovies() {
        return movieService.getAllMovies();
    }

    @GetMapping("/{movieId}")
    public MovieDetailResponseDto getMovieDetailById(@PathVariable Long movieId) {
        return movieService.getMovieDetailById(movieId);
    }
}