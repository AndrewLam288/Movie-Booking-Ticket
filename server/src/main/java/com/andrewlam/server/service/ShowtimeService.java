package com.andrewlam.server.service;

import com.andrewlam.server.dto.response.ShowtimeDetailResponseDto;
import com.andrewlam.server.dto.response.ShowtimeSummaryResponseDto;

import java.util.List;

public interface ShowtimeService {

    List<ShowtimeSummaryResponseDto> getShowtimesByCinemaId(Long cinemaId);

    List<ShowtimeSummaryResponseDto> getShowtimesByMovieId(Long movieId);

    ShowtimeDetailResponseDto getShowtimeById(Long showtimeId);
}