package com.andrewlam.server.controller;

import com.andrewlam.server.dto.response.ShowtimeDetailResponseDto;
import com.andrewlam.server.dto.response.ShowtimeSummaryResponseDto;
import com.andrewlam.server.service.ShowtimeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ShowtimeController {

    private final ShowtimeService showtimeService;

    public ShowtimeController(ShowtimeService showtimeService) {
        this.showtimeService = showtimeService;
    }

    @GetMapping("/cinemas/{cinemaId}/showtimes")
    public ResponseEntity<List<ShowtimeSummaryResponseDto>> getShowtimesByCinemaId(@PathVariable Long cinemaId) {
        return ResponseEntity.ok(showtimeService.getShowtimesByCinemaId(cinemaId));
    }

    @GetMapping("/showtimes/{showtimeId}")
    public ResponseEntity<ShowtimeDetailResponseDto> getShowtimeById(@PathVariable Long showtimeId) {
        return ResponseEntity.ok(showtimeService.getShowtimeById(showtimeId));
    }
}