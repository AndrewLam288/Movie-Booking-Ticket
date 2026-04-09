package com.andrewlam.server.mapper;

import com.andrewlam.server.dto.response.MovieDetailResponseDto;
import com.andrewlam.server.dto.response.MovieSummaryResponseDto;
import com.andrewlam.server.model.Movie;
import org.springframework.stereotype.Component;

@Component
public class MovieMapper {

    public MovieSummaryResponseDto toMovieSummaryResponseDto(Movie movie) {
        return new MovieSummaryResponseDto(
                movie.getId(),
                movie.getTitle(),
                movie.getDurationMinutes(),
                movie.getLanguage(),
                movie.getPosterUrl(),
                movie.getReleaseDate(),
                movie.getAgeRating(),
                movie.getStatus()
        );
    }

    public MovieDetailResponseDto toMovieDetailResponseDto(Movie movie) {
        return new MovieDetailResponseDto(
                movie.getId(),
                movie.getTitle(),
                movie.getDescription(),
                movie.getDurationMinutes(),
                movie.getLanguage(),
                movie.getDirector(),
                movie.getCastMembers(),
                movie.getPosterUrl(),
                movie.getBannerUrl(),
                movie.getTrailerUrl(),
                movie.getReleaseDate(),
                movie.getAgeRating(),
                movie.getStatus()
        );
    }
}