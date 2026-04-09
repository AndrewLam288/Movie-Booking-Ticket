package com.andrewlam.server.dto.response;

import com.andrewlam.server.enums.AgeRating;
import com.andrewlam.server.enums.MovieStatus;

import java.time.LocalDate;

public record MovieDetailResponseDto(
        Long id,
        String title,
        String description,
        Integer durationMinutes,
        String language,
        String director,
        String castMembers,
        String posterUrl,
        String bannerUrl,
        String trailerUrl,
        LocalDate releaseDate,
        AgeRating ageRating,
        MovieStatus status
) {

}