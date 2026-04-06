package com.andrewlam.server.common.response;

public record FieldErrorResponse (
        String field,
        String message
) {

}