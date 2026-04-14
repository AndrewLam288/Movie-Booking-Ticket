package com.andrewlam.server.controller;

import com.andrewlam.server.dto.response.CinemaDetailResponseDto;
import com.andrewlam.server.dto.response.CinemaSummaryResponseDto;
import com.andrewlam.server.service.CinemaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cinemas")
public class CinemaController {

    private final CinemaService cinemaService;

    public CinemaController(CinemaService cinemaService) {
        this.cinemaService = cinemaService;
    }

    @GetMapping
    public ResponseEntity<List<CinemaSummaryResponseDto>> getAllCinemas() {
        return ResponseEntity.ok(cinemaService.getAllCinemas());
    }

    @GetMapping("/{cinemaId}")
    public ResponseEntity<CinemaDetailResponseDto> getCinemaById(@PathVariable Long cinemaId) {
        return ResponseEntity.ok(cinemaService.getCinemaById(cinemaId));
    }
}