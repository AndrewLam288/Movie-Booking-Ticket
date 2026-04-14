package com.andrewlam.server.service;

import com.andrewlam.server.dto.response.CinemaDetailResponseDto;
import com.andrewlam.server.dto.response.CinemaSummaryResponseDto;

import java.util.List;

public interface CinemaService {

    List<CinemaSummaryResponseDto> getAllCinemas();

    CinemaDetailResponseDto getCinemaById(Long cinemaId);
}