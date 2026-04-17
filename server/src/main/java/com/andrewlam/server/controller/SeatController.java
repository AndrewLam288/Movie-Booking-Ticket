package com.andrewlam.server.controller;

import com.andrewlam.server.dto.request.HoldSeatRequestDto;
import com.andrewlam.server.dto.request.ReleaseSeatRequestDto;
import com.andrewlam.server.dto.response.SeatMapResponseDto;
import com.andrewlam.server.service.SeatService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/showtimes/{showtimeId}/seats")
public class SeatController {

    private final SeatService seatService;

    public SeatController(SeatService seatService) {
        this.seatService = seatService;
    }

    @GetMapping
    public ResponseEntity<SeatMapResponseDto> getSeatMap(@PathVariable Long showtimeId) {
        return ResponseEntity.ok(seatService.getSeatMap(showtimeId));
    }

    @PostMapping("/hold")
    public ResponseEntity<Void> holdSeat(
            @PathVariable Long showtimeId,
            @Valid @RequestBody HoldSeatRequestDto requestDto
    ) {
        seatService.holdSeat(showtimeId, requestDto.getSeatId(), requestDto.getClientSessionId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/release")
    public ResponseEntity<Void> releaseSeat(
            @PathVariable Long showtimeId,
            @Valid @RequestBody ReleaseSeatRequestDto requestDto
    ) {
        seatService.releaseSeat(showtimeId, requestDto.getSeatId(), requestDto.getClientSessionId());
        return ResponseEntity.ok().build();
    }
}