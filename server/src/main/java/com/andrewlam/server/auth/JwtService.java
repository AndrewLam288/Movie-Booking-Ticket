package com.andrewlam.server.auth;

import com.andrewlam.server.enums.UserRole;
import com.andrewlam.server.model.User;

public interface JwtService {
    String generateAccessToken(User user);
    String extractEmail(String token);
    Long extractUserId(String token);
    UserRole extractRole(String token);
    boolean isTokenValid(String token, String expectedEmail);
}