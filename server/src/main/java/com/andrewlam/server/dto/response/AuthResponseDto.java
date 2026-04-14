package com.andrewlam.server.dto.response;

import com.andrewlam.server.enums.UserRole;

public record AuthResponseDto(
    String accessToken,
    String tokenType,
    Long userId,
    String email,
    String fullName,
    UserRole role
) {}