package com.andrewlam.server.common.exception;

// Throw when register with an email exists in the DB
public class EmailAlreadyExistsException extends ConflictException {
    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}