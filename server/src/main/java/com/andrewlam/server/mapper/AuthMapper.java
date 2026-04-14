package com.andrewlam.server.mapper;

import com.andrewlam.server.dto.response.AuthResponseDto;
import com.andrewlam.server.dto.response.CurrentUserResponseDto;
import com.andrewlam.server.model.User;
import org.springframework.stereotype.Component;

@Component
public class AuthMapper {

    public AuthResponseDto toAuthResponseDto(User user, String accessToken) {
        return new AuthResponseDto(
                accessToken,
                "Bearer",
                user.getId(),
                user.getEmail(),
                user.getFullName(),
                user.getRole()
        );
    }

    public CurrentUserResponseDto toCurrentUserResponseDto(User user) {
        return new CurrentUserResponseDto(
                user.getId(),
                user.getEmail(),
                user.getFullName(),
                user.getPhoneNumber(),
                user.getRole()
        );
    }
}