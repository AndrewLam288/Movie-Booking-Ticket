package com.andrewlam.server.controller;

import com.andrewlam.server.dto.response.SeatMapResponseDto;
import com.andrewlam.server.model.User;
import com.andrewlam.server.repository.UserRepository;
import com.andrewlam.server.service.SeatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/showtimes")
public class SeatController {

    private final SeatService seatService;
    private final UserRepository userRepository;

    public SeatController(SeatService seatService, UserRepository userRepository) {
        this.seatService = seatService;
        this.userRepository = userRepository;
    }

    @GetMapping("/{showtimeId}/seats")
    public ResponseEntity<SeatMapResponseDto> getSeatMap(@PathVariable Long showtimeId) {
        return ResponseEntity.ok(seatService.getSeatMap(showtimeId));
    }

    @PostMapping("/{showtimeId}/seats/{seatId}/hold")
    public ResponseEntity<Void> holdSeat(
            @PathVariable Long showtimeId,
            @PathVariable Long seatId,
            Authentication authentication
    ) {
        User user = resolveAuthenticatedUser(authentication);
        String holderKey = buildHolderKey(user.getId());

        seatService.holdSeat(showtimeId, seatId, holderKey);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{showtimeId}/seats/{seatId}/hold")
    public ResponseEntity<Void> releaseSeat(
            @PathVariable Long showtimeId,
            @PathVariable Long seatId,
            Authentication authentication
    ) {
        User user = resolveAuthenticatedUser(authentication);
        String holderKey = buildHolderKey(user.getId());

        seatService.releaseSeat(showtimeId, seatId, holderKey);
        return ResponseEntity.noContent().build();
    }

    private User resolveAuthenticatedUser(Authentication authentication) {
        if (authentication == null || authentication.getName() == null || authentication.getName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication is required.");
        }

        String principalName = authentication.getName();

        return userRepository.findByEmailIgnoreCase(principalName)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Authenticated user not found."
                ));
    }

    private String buildHolderKey(Long userId) {
        return "USER:" + userId;
    }
}