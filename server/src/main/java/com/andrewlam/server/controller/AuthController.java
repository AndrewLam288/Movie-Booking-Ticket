package com.andrewlam.server.controller;

import com.andrewlam.server.auth.UserPrincipal;
import com.andrewlam.server.dto.response.AuthResponseDto;
import com.andrewlam.server.dto.response.CurrentUserResponseDto;
import com.andrewlam.server.dto.request.LoginRequestDto;
import com.andrewlam.server.dto.request.RegisterRequestDto;
import com.andrewlam.server.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(@Valid @RequestBody RegisterRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody LoginRequestDto request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping("/me")
    public ResponseEntity<CurrentUserResponseDto> getCurrentUser(
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        return ResponseEntity.ok(authService.getCurrentUser(principal));
    }
}