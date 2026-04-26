package com.andrewlam.server.service.impl;

import com.andrewlam.server.common.exception.ResourceNotFoundException;
import com.andrewlam.server.dto.response.ShowtimeDetailResponseDto;
import com.andrewlam.server.dto.response.ShowtimeSummaryResponseDto;
import com.andrewlam.server.mapper.ShowtimeMapper;
import com.andrewlam.server.model.Cinema;
import com.andrewlam.server.model.Movie;
import com.andrewlam.server.model.Showtime;
import com.andrewlam.server.repository.CinemaRepository;
import com.andrewlam.server.repository.MovieRepository;
import com.andrewlam.server.repository.ShowtimeRepository;
import com.andrewlam.server.service.ShowtimeService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ShowtimeServiceImpl implements ShowtimeService {

    private final ShowtimeRepository showtimeRepository;
    private final CinemaRepository cinemaRepository;
    private final ShowtimeMapper showtimeMapper;
    private final MovieRepository movieRepository;

    public ShowtimeServiceImpl(
            ShowtimeRepository showtimeRepository,
            CinemaRepository cinemaRepository,
            ShowtimeMapper showtimeMapper,
            MovieRepository movieRepository) {
        this.showtimeRepository = showtimeRepository;
        this.cinemaRepository = cinemaRepository;
        this.showtimeMapper = showtimeMapper;
        this.movieRepository = movieRepository;
    }

    @Override
    @Cacheable(cacheNames = "showtimesByCinema", key = "#cinemaId")
    public List<ShowtimeSummaryResponseDto> getShowtimesByCinemaId(Long cinemaId) {
        Cinema cinema = cinemaRepository.findByIdAndIsActiveTrue(cinemaId)
                .orElseThrow(() -> new ResourceNotFoundException("Cinema not found with id: " + cinemaId));

        return showtimeRepository.findAllByRoomCinemaIdOrderByStartTimeAsc(cinema.getId())
                .stream()
                .map(showtimeMapper::toSummaryResponseDto)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    @Cacheable(cacheNames = "showtimesByMovie", key = "#movieId")
    public List<ShowtimeSummaryResponseDto> getShowtimesByMovieId(Long movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + movieId));

        return showtimeRepository.findAllByMovieIdOrderByStartTimeAsc(movie.getId())
                .stream()
                .map(showtimeMapper::toSummaryResponseDto)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    @Cacheable(cacheNames = "showtimeDetail", key = "#showtimeId")
    public ShowtimeDetailResponseDto getShowtimeById(Long showtimeId) {
        Showtime showtime = showtimeRepository.findById(showtimeId)
                .orElseThrow(() -> new ResourceNotFoundException("Showtime not found with id: " + showtimeId));

        return showtimeMapper.toDetailResponseDto(showtime);
    }
}
