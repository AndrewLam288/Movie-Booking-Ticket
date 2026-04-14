package com.andrewlam.server.dto.response;

public record CinemaSummaryResponseDto(
        Long id,
        String name,
        String addressLine,
        String city,
        String state,
        String postalCode,
        String country,
        Boolean isActive
) {
}