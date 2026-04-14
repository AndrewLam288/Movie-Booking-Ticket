package com.andrewlam.server.service;

import com.andrewlam.server.auth.UserPrincipal;
import com.andrewlam.server.dto.response.AuthResponseDto;
import com.andrewlam.server.dto.response.CurrentUserResponseDto;
import com.andrewlam.server.dto.request.LoginRequestDto;
import com.andrewlam.server.dto.request.RegisterRequestDto;

public interface AuthService {

    AuthResponseDto register(RegisterRequestDto request);

    AuthResponseDto login(LoginRequestDto request);

    CurrentUserResponseDto getCurrentUser(UserPrincipal principal);
}