package com.andrewlam.server.mapper;

import com.andrewlam.server.dto.response.ShowtimeDetailResponseDto;
import com.andrewlam.server.dto.response.ShowtimeSummaryResponseDto;
import com.andrewlam.server.model.Showtime;
import org.springframework.stereotype.Component;

@Component
public class ShowtimeMapper {

    public ShowtimeSummaryResponseDto toSummaryResponseDto(Showtime showtime) {
        return new ShowtimeSummaryResponseDto(
                showtime.getId(),
                showtime.getMovie().getId(),
                showtime.getMovie().getTitle(),
                showtime.getRoom().getCinema().getId(),
                showtime.getRoom().getCinema().getName(),
                showtime.getRoom().getId(),
                showtime.getRoom().getName(),
                showtime.getFormat(),
                showtime.getStartTime(),
                showtime.getEndTime(),
                showtime.getBasePrice(),
                showtime.getStatus()
        );
    }

    public ShowtimeDetailResponseDto toDetailResponseDto(Showtime showtime) {
        return new ShowtimeDetailResponseDto(
                showtime.getId(),
                showtime.getMovie().getId(),
                showtime.getMovie().getTitle(),
                showtime.getRoom().getCinema().getId(),
                showtime.getRoom().getCinema().getName(),
                showtime.getRoom().getCinema().getAddressLine(),
                showtime.getRoom().getCinema().getCity(),
                showtime.getRoom().getCinema().getState(),
                showtime.getRoom().getCinema().getPostalCode(),
                showtime.getRoom().getCinema().getCountry(),
                showtime.getRoom().getId(),
                showtime.getRoom().getName(),
                showtime.getFormat(),
                showtime.getStartTime(),
                showtime.getEndTime(),
                showtime.getBasePrice(),
                showtime.getStatus()
        );
    }
}