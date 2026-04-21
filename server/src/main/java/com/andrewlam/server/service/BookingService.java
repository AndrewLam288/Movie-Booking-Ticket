package com.andrewlam.server.service;

import com.andrewlam.server.dto.request.CreateBookingRequestDto;
import com.andrewlam.server.dto.response.BookingResponseDto;

import java.util.List;

public interface BookingService {

    BookingResponseDto createBooking(CreateBookingRequestDto requestDto, String principalName);

    BookingResponseDto getMyBooking(String bookingCode, String principalName);

    List<BookingResponseDto> getMyBookings(String principalName);
}