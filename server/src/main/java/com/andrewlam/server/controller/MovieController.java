package com.andrewlam.server.controller;

import com.andrewlam.server.dto.response.MovieDetailResponseDto;
import com.andrewlam.server.dto.response.MovieSummaryResponseDto;
import com.andrewlam.server.dto.response.ShowtimeSummaryResponseDto;
import com.andrewlam.server.service.MovieService;
import com.andrewlam.server.service.ShowtimeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/movies")
public class MovieController {

    private final MovieService movieService;
    private final ShowtimeService showtimeService;

    public MovieController(MovieService movieService, ShowtimeService showtimeService) {
        this.movieService = movieService;
        this.showtimeService = showtimeService;
    }

    @GetMapping
    public List<MovieSummaryResponseDto> getAllMovies() {
        return movieService.getAllMovies();
    }

    @GetMapping("/{movieId}")
    public MovieDetailResponseDto getMovieDetailById(@PathVariable Long movieId) {
        return movieService.getMovieDetailById(movieId);
    }

    @GetMapping("/{movieId}/showtimes")
    public List<ShowtimeSummaryResponseDto> getShowtimesByMovieId(@PathVariable Long movieId) {
        return showtimeService.getShowtimesByMovieId(movieId);
    }
}