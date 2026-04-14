package com.andrewlam.server.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequestDto(
        @NotBlank(message = "Email is required.")
        @Email(message = "Email must be a valid email address.")
        @Size(max = 255, message = "Email must not exceed 255 characters.")
        String email,

        @NotBlank(message = "Password is required.")
        @Size(min = 8, max = 255, message = "Password must be between 8 and 100 characters.")
        String password,

        @NotBlank(message = "Full name is required.")
        @Size(max = 255, message = "Full name must not exceed 255 characters.")
        String fullName,

        @Size(max = 20, message = "Phone number must not exceed 20 characters.")
        String phoneNumber
) {}