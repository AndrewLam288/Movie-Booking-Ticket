package com.andrewlam.server.service.impl;

import com.andrewlam.server.auth.JwtService;
import com.andrewlam.server.auth.UserPrincipal;
import com.andrewlam.server.common.exception.EmailAlreadyExistsException;
import com.andrewlam.server.common.exception.InvalidCredentialsException;
import com.andrewlam.server.dto.response.AuthResponseDto;
import com.andrewlam.server.dto.response.CurrentUserResponseDto;
import com.andrewlam.server.dto.request.LoginRequestDto;
import com.andrewlam.server.dto.request.RegisterRequestDto;
import com.andrewlam.server.enums.UserRole;
import com.andrewlam.server.mapper.AuthMapper;
import com.andrewlam.server.model.User;
import com.andrewlam.server.repository.UserRepository;
import com.andrewlam.server.service.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final AuthMapper authMapper;

    public AuthServiceImpl(UserRepository userRepository,
                           org.springframework.security.crypto.password.PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager,
                           JwtService jwtService,
                           AuthMapper authMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.authMapper = authMapper;
    }

    @Override
    public AuthResponseDto register(RegisterRequestDto request) {
        String normalizedEmail = normalizeEmail(request.email());

        if (userRepository.existsByEmailIgnoreCase(normalizedEmail)) {
            throw new EmailAlreadyExistsException("An account with that email already exists.");
        }

        User user = new User();
        user.setEmail(normalizedEmail);
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setFullName(normalizeText(request.fullName()));
        user.setPhoneNumber(normalizePhoneNumber(request.phoneNumber()));
        user.setRole(UserRole.CUSTOMER);
        user.setIsActive(true);

        User savedUser = userRepository.save(user);

        String accessToken = jwtService.generateAccessToken(savedUser);
        return authMapper.toAuthResponseDto(savedUser, accessToken);
    }

    @Override
    public AuthResponseDto login(LoginRequestDto request) {
        String normalizedEmail = normalizeEmail(request.email());

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            normalizedEmail,
                            request.password()
                    )
            );

            UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

            User user = userRepository.findById(principal.getId())
                    .orElseThrow(() -> new InvalidCredentialsException("Authenticated user no longer exists."));

            String accessToken = jwtService.generateAccessToken(user);
            return authMapper.toAuthResponseDto(user, accessToken);
        } catch (AuthenticationException ex) {
            throw new InvalidCredentialsException("Invalid email or password.");
        }
    }

    @Override
    public CurrentUserResponseDto getCurrentUser(UserPrincipal principal) {
        if (principal == null) {
            throw new InvalidCredentialsException("Authenticated user not found.");
        }

        User user = userRepository.findById(principal.getId())
                .orElseThrow(() -> new InvalidCredentialsException("Authenticated user no longer exists."));

        return authMapper.toCurrentUserResponseDto(user);
    }

    private String normalizeEmail(String email) {
        return email == null ? null : email.trim().toLowerCase();
    }

    private String normalizeText(String value) {
        return value == null ? null : value.trim();
    }

    private String normalizePhoneNumber(String phoneNumber) {
        if (phoneNumber == null) {
            return null;
        }

        String trimmedValue = phoneNumber.trim();
        return trimmedValue.isEmpty() ? null : trimmedValue;
    }
}