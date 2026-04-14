package com.andrewlam.server.common.exception;

// Throw when login failed
public class InvalidCredentialsException extends UnauthorizedException {
    public InvalidCredentialsException(String message) {
        super(message);
    }
}