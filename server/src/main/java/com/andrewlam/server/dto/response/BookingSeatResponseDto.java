package com.andrewlam.server.dto.response;

import java.math.BigDecimal;

public class BookingSeatResponseDto {

    private Long seatId;
    private String seatLabel;
    private BigDecimal price;

    public BookingSeatResponseDto() {
    }

    public BookingSeatResponseDto(Long seatId, String seatLabel, BigDecimal price) {
        this.seatId = seatId;
        this.seatLabel = seatLabel;
        this.price = price;
    }

    public Long getSeatId() {
        return seatId;
    }

    public void setSeatId(Long seatId) {
        this.seatId = seatId;
    }

    public String getSeatLabel() {
        return seatLabel;
    }

    public void setSeatLabel(String seatLabel) {
        this.seatLabel = seatLabel;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}