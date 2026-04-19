package com.andrewlam.server.dto.response;

import com.andrewlam.server.enums.BookingStatus;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public class BookingResponseDto {

    private Long bookingId;
    private String bookingCode;
    private Long showtimeId;
    private BookingStatus status;
    private BigDecimal totalAmount;
    private OffsetDateTime bookedAt;
    private List<BookingSeatResponseDto> seats;

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public String getBookingCode() {
        return bookingCode;
    }

    public void setBookingCode(String bookingCode) {
        this.bookingCode = bookingCode;
    }

    public Long getShowtimeId() {
        return showtimeId;
    }

    public void setShowtimeId(Long showtimeId) {
        this.showtimeId = showtimeId;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public OffsetDateTime getBookedAt() {
        return bookedAt;
    }

    public void setBookedAt(OffsetDateTime bookedAt) {
        this.bookedAt = bookedAt;
    }

    public List<BookingSeatResponseDto> getSeats() {
        return seats;
    }

    public void setSeats(List<BookingSeatResponseDto> seats) {
        this.seats = seats;
    }
}