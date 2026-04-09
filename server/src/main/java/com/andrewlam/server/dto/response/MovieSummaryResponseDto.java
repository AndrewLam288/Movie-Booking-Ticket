package com.andrewlam.server.dto.response;

import com.andrewlam.server.enums.AgeRating;
import com.andrewlam.server.enums.MovieStatus;

import java.time.LocalDate;

public record MovieSummaryResponseDto(
    Long id,
    String title,
    Integer durationMinutes,
    String language,
    String posterUrl,
    LocalDate releaseDate,
    AgeRating ageRating,
    MovieStatus status
) {

}