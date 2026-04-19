package com.andrewlam.server.mapper;

import com.andrewlam.server.dto.response.BookingResponseDto;
import com.andrewlam.server.dto.response.BookingSeatResponseDto;
import com.andrewlam.server.model.Booking;
import com.andrewlam.server.model.BookingSeat;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookingMapper {

    public BookingResponseDto toResponseDto(Booking booking) {
        BookingResponseDto dto = new BookingResponseDto();
        dto.setBookingId(booking.getId());
        dto.setBookingCode(booking.getBookingCode());
        dto.setShowtimeId(booking.getShowtime().getId());
        dto.setStatus(booking.getStatus());
        dto.setTotalAmount(booking.getTotalAmount());
        dto.setBookedAt(booking.getBookedAt());

        List<BookingSeatResponseDto> seats = booking.getBookingSeats()
                .stream()
                .map(this::toSeatResponseDto)
                .toList();

        dto.setSeats(seats);
        return dto;
    }

    private BookingSeatResponseDto toSeatResponseDto(BookingSeat bookingSeat) {
        String seatLabel = bookingSeat.getSeat().getSeatRow() + bookingSeat.getSeat().getSeatNumber();
        return new BookingSeatResponseDto(
                bookingSeat.getSeat().getId(),
                seatLabel,
                bookingSeat.getPrice()
        );
    }
}