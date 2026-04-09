package com.andrewlam.server.common.exception;

public class MovieNotFoundException extends ResourceNotFoundException {

    public MovieNotFoundException(Long movieId) {
        super("Movie not found with id: " + movieId);
    }
}