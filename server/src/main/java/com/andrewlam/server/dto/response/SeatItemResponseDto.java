package com.andrewlam.server.dto.response;

import com.andrewlam.server.enums.SeatAvailabilityStatus;
import com.andrewlam.server.enums.SeatType;

public class SeatItemResponseDto {

    private Long seatId;
    private String seatRow;
    private int seatNumber;
    private SeatType seatType;
    private Boolean isActive;
    private SeatAvailabilityStatus status;
    private String heldBySessionId;

    public Long getSeatId() {
        return seatId;
    }

    public void setSeatId(Long seatId) {
        this.seatId = seatId;
    }

    public String getSeatRow() {
        return seatRow;
    }

    public void setSeatRow(String seatRow) {
        this.seatRow = seatRow;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    public SeatType getSeatType() {
        return seatType;
    }

    public void setSeatType(SeatType seatType) {
        this.seatType = seatType;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }

    public SeatAvailabilityStatus getStatus() {
        return status;
    }

    public void setStatus(SeatAvailabilityStatus status) {
        this.status = status;
    }

    public String getHeldBySessionId() {
        return heldBySessionId;
    }

    public void setHeldBySessionId(String heldBySessionId) {
        this.heldBySessionId = heldBySessionId;
    }
}