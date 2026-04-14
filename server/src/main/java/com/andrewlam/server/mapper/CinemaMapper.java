package com.andrewlam.server.mapper;

import com.andrewlam.server.dto.response.CinemaDetailResponseDto;
import com.andrewlam.server.dto.response.CinemaSummaryResponseDto;
import com.andrewlam.server.model.Cinema;
import org.springframework.stereotype.Component;

@Component
public class CinemaMapper {

    public CinemaSummaryResponseDto toSummaryResponseDto(Cinema cinema) {
        return new CinemaSummaryResponseDto(
                cinema.getId(),
                cinema.getName(),
                cinema.getAddressLine(),
                cinema.getCity(),
                cinema.getState(),
                cinema.getPostalCode(),
                cinema.getCountry(),
                cinema.getIsActive()
        );
    }

    public CinemaDetailResponseDto toDetailResponseDto(Cinema cinema) {
        return new CinemaDetailResponseDto(
                cinema.getId(),
                cinema.getName(),
                cinema.getAddressLine(),
                cinema.getCity(),
                cinema.getState(),
                cinema.getPostalCode(),
                cinema.getCountry(),
                cinema.getIsActive()
        );
    }
}