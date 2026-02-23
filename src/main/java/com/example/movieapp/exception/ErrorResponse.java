package com.example.movieapp.exception;

public record ErrorResponse(
    String error,
    Object details
) {
    public ErrorResponse(String error) {
        this(error, null);
    }
}