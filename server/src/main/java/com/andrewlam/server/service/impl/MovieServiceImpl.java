package com.andrewlam.server.service.impl;

import com.andrewlam.server.common.exception.MovieNotFoundException;
import com.andrewlam.server.dto.response.MovieDetailResponseDto;
import com.andrewlam.server.dto.response.MovieSummaryResponseDto;
import com.andrewlam.server.mapper.MovieMapper;
import com.andrewlam.server.model.Movie;
import com.andrewlam.server.repository.MovieRepository;
import com.andrewlam.server.service.MovieService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;

    public MovieServiceImpl(MovieRepository movieRepository, MovieMapper movieMapper) {
        this.movieRepository = movieRepository;
        this.movieMapper = movieMapper;
    }

    @Override
    public List<MovieSummaryResponseDto> getAllMovies() {
        return movieRepository.findAllByOrderByReleaseDateDesc()
                .stream()
                .map(movieMapper::toMovieSummaryResponseDto)
                .toList();
    }

    @Override
    public MovieDetailResponseDto getMovieDetailById(Long movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new MovieNotFoundException(movieId));

        return movieMapper.toMovieDetailResponseDto(movie);
    }
}