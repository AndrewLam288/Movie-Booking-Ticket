package com.andrewlam.server.dto;

import com.andrewlam.server.enums.SeatAvailabilityStatus;

public class SeatAvailabilityEventDto {

    private Long showtimeId;
    private Long seatId;
    private SeatAvailabilityStatus status;
    private String clientSessionId;

    public Long getShowtimeId() {
        return showtimeId;
    }

    public void setShowtimeId(Long showtimeId) {
        this.showtimeId = showtimeId;
    }

    public Long getSeatId() {
        return seatId;
    }

    public void setSeatId(Long seatId) {
        this.seatId = seatId;
    }

    public SeatAvailabilityStatus getStatus() {
        return status;
    }

    public void setStatus(SeatAvailabilityStatus status) {
        this.status = status;
    }

    public String getClientSessionId() {
        return clientSessionId;
    }

    public void setClientSessionId(String clientSessionId) {
        this.clientSessionId = clientSessionId;
    }
}