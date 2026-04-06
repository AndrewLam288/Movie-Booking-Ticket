package com.andrewlam.server.common.exception;

import com.andrewlam.server.common.response.ApiErrorResponse;
import com.andrewlam.server.common.response.FieldErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handles custom application exceptions such as not found, conflict, bad request.
    @ExceptionHandler (ApiException.class)
    public ResponseEntity<ApiErrorResponse> handleApiException(ApiException ex, HttpServletRequest request) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(
                Instant.now(),
                ex.getStatus().value(),
                ex.getStatus().getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI(),
                List.of()
        );

        return ResponseEntity.status(ex.getStatus()).body(errorResponse);
    }

    // Handles validation errors for request body fields, usually from @Valid on DTOs.
    @ExceptionHandler (MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValidException
            (MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<FieldErrorResponse> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new FieldErrorResponse(
                        error.getField(),
                        error.getDefaultMessage()
                )).toList();

        ApiErrorResponse errorResponse = new ApiErrorResponse(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Validation Failed",
                request.getRequestURI(),
                fieldErrors
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    // Handles validation error for method parameters or direct constraint violations.
    @ExceptionHandler (ConstraintViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleConstraintViolationException
            (ConstraintViolationException ex, HttpServletRequest request) {
        List<FieldErrorResponse> fieldErrors = ex.getConstraintViolations()
                .stream()
                .map(this::toFieldError)
                .toList();

        ApiErrorResponse errorResponse = new ApiErrorResponse(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Validation Failed",
                request.getRequestURI(),
                fieldErrors
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    // Helper method for validation violation
    // Example use case: property path = "email" message = "must be a well-formed email address"
    // -> turn to be new FieldErrorResponse("email", "must be a well-formed email address")
    private FieldErrorResponse toFieldError(ConstraintViolation<?> violation) {
        return new FieldErrorResponse(
                violation.getPropertyPath().toString(),
                violation.getMessage()
        );
    }

    // Final fallback for any unhandled exception in the application.
    @ExceptionHandler (Exception.class)
    public ResponseEntity<ApiErrorResponse> handleException(Exception ex, HttpServletRequest request) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(
          Instant.now(),
          HttpStatus.INTERNAL_SERVER_ERROR.value(),
          HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
          "An unexpected error occurred",
          request.getRequestURI(),
          List.of()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}