package com.etsia.common.infrastructure.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

/**
 * Standard error response structure for API errors.
 * Contains all required fields for consistent error handling.
 */
public record ErrorResponse(
    String message,
    String errorCode,
    int status,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime timestamp,
    String path
) {
    /**
     * Factory method to create an ErrorResponse with current timestamp.
     */
    public static ErrorResponse of(String message, String errorCode, HttpStatus status, String path) {
        return new ErrorResponse(message, errorCode, status.value(), LocalDateTime.now(), path);
    }

    /**
     * Factory method to create an ErrorResponse with a specific timestamp.
     */
    public static ErrorResponse of(String message, String errorCode, int status, LocalDateTime timestamp, String path) {
        return new ErrorResponse(message, errorCode, status, timestamp, path);
    }
}
