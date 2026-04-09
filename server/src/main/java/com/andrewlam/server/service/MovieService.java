package com.andrewlam.server.service;

import com.andrewlam.server.dto.response.MovieDetailResponseDto;
import com.andrewlam.server.dto.response.MovieSummaryResponseDto;

import java.util.List;

public interface MovieService {

    List<MovieSummaryResponseDto> getAllMovies();

    MovieDetailResponseDto getMovieDetailById(Long movieId);
}