package com.andrewlam.server.controller;

import com.andrewlam.server.dto.request.CreateBookingRequestDto;
import com.andrewlam.server.dto.response.BookingResponseDto;
import com.andrewlam.server.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<BookingResponseDto> createBooking(
            @Valid @RequestBody CreateBookingRequestDto requestDto,
            Authentication authentication
    ) {
        BookingResponseDto responseDto = bookingService.createBooking(requestDto, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping("/{bookingCode}")
    public ResponseEntity<BookingResponseDto> getMyBooking(
            @PathVariable String bookingCode,
            Authentication authentication
    ) {
        BookingResponseDto responseDto = bookingService.getMyBooking(bookingCode, authentication.getName());
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/me")
    public ResponseEntity<List<BookingResponseDto>> getMyBookings(Authentication authentication) {
        List<BookingResponseDto> response = bookingService.getMyBookings(authentication.getName());
        return ResponseEntity.ok(response);
    }
}