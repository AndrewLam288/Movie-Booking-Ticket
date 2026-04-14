package com.andrewlam.server.dto.response;

import com.andrewlam.server.enums.ShowtimeFormat;
import com.andrewlam.server.enums.ShowtimeStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ShowtimeSummaryResponseDto(
        Long id,
        Long movieId,
        String movieTitle,
        Long cinemaId,
        String cinemaName,
        Long roomId,
        String roomName,
        ShowtimeFormat format,
        LocalDateTime startTime,
        LocalDateTime endTime,
        BigDecimal basePrice,
        ShowtimeStatus status
) {
}