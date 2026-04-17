package com.andrewlam.server.service.impl;

import com.andrewlam.server.common.exception.ResourceNotFoundException;
import com.andrewlam.server.dto.response.CinemaDetailResponseDto;
import com.andrewlam.server.dto.response.CinemaSummaryResponseDto;
import com.andrewlam.server.mapper.CinemaMapper;
import com.andrewlam.server.model.Cinema;
import com.andrewlam.server.repository.CinemaRepository;
import com.andrewlam.server.service.CinemaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CinemaServiceImpl implements CinemaService {

    private final CinemaRepository cinemaRepository;
    private final CinemaMapper cinemaMapper;

    public CinemaServiceImpl(CinemaRepository cinemaRepository, CinemaMapper cinemaMapper) {
        this.cinemaRepository = cinemaRepository;
        this.cinemaMapper = cinemaMapper;
    }

    @Override
    public List<CinemaSummaryResponseDto> getAllCinemas() {
        return cinemaRepository.findAllByIsActiveTrueOrderByNameAsc()
                .stream()
                .map(cinemaMapper::toSummaryResponseDto)
                .toList();
    }

    @Override
    public CinemaDetailResponseDto getCinemaById(Long cinemaId) {
        Cinema cinema = cinemaRepository.findByIdAndIsActiveTrue(cinemaId)
                .orElseThrow(() -> new ResourceNotFoundException("Cinema not found with id: " + cinemaId));

        return cinemaMapper.toDetailResponseDto(cinema);
    }
}