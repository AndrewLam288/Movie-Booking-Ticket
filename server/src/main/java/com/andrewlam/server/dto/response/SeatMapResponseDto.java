package com.andrewlam.server.dto.response;

import java.util.List;

public class SeatMapResponseDto {

    private Long showtimeId;
    private Long roomId;
    private String roomName;
    private List<SeatItemResponseDto> seats;

    public Long getShowtimeId() {
        return showtimeId;
    }

    public void setShowtimeId(Long showtimeId) {
        this.showtimeId = showtimeId;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public List<SeatItemResponseDto> getSeats() {
        return seats;
    }

    public void setSeats(List<SeatItemResponseDto> seats) {
        this.seats = seats;
    }
}