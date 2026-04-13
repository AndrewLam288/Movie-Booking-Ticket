package com.andrewlam.server.dto.response;

import com.andrewlam.server.enums.UserRole;

public record CurrentUserResponseDto(
   Long userId,
   String email,
   String fullName,
   String phoneNumber,
   UserRole role
) {}