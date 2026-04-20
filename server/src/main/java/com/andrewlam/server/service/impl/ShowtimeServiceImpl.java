package com.andrewlam.server.service.impl;

import com.andrewlam.server.common.exception.ResourceNotFoundException;
import com.andrewlam.server.dto.response.ShowtimeDetailResponseDto;
import com.andrewlam.server.dto.response.ShowtimeSummaryResponseDto;
import com.andrewlam.server.mapper.ShowtimeMapper;
import com.andrewlam.server.model.Cinema;
import com.andrewlam.server.model.Showtime;
import com.andrewlam.server.repository.CinemaRepository;
import com.andrewlam.server.repository.ShowtimeRepository;
import com.andrewlam.server.service.ShowtimeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ShowtimeServiceImpl implements ShowtimeService {

    private final ShowtimeRepository showtimeRepository;
    private final CinemaRepository cinemaRepository;
    private final ShowtimeMapper showtimeMapper;

    public ShowtimeServiceImpl(
            ShowtimeRepository showtimeRepository,
            CinemaRepository cinemaRepository,
            ShowtimeMapper showtimeMapper
    ) {
        this.showtimeRepository = showtimeRepository;
        this.cinemaRepository = cinemaRepository;
        this.showtimeMapper = showtimeMapper;
    }

    @Override
    public List<ShowtimeSummaryResponseDto> getShowtimesByCinemaId(Long cinemaId) {
        Cinema cinema = cinemaRepository.findByIdAndIsActiveTrue(cinemaId)
                .orElseThrow(() -> new ResourceNotFoundException("Cinema not found with id: " + cinemaId));

        return showtimeRepository.findAllByRoomCinemaIdOrderByStartTimeAsc(cinema.getId())
                .stream()
                .map(showtimeMapper::toSummaryResponseDto)
                .toList();
    }

    @Override
    public List<ShowtimeSummaryResponseDto> getShowtimesByMovieId(Long movieId) {
        return showtimeRepository.findAllByMovieIdOrderByStartTimeAsc(movieId)
                .stream()
                .map(showtimeMapper::toSummaryResponseDto)
                .toList();
    }

    @Override
    public ShowtimeDetailResponseDto getShowtimeById(Long showtimeId) {
        Showtime showtime = showtimeRepository.findById(showtimeId)
                .orElseThrow(() -> new ResourceNotFoundException("Showtime not found with id: " + showtimeId));

        return showtimeMapper.toDetailResponseDto(showtime);
    }
}