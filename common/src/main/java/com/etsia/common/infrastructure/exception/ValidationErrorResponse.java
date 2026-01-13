package com.etsia.common.infrastructure.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Extended error response for validation errors with field-level details.
 */
public record ValidationErrorResponse(
    String message,
    String errorCode,
    int status,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime timestamp,
    String path,
    Map<String, String> fieldErrors
) {
    /**
     * Factory method to create a ValidationErrorResponse with current timestamp.
     */
    public static ValidationErrorResponse of(String message, Map<String, String> fieldErrors, String path) {
        return new ValidationErrorResponse(
            message,
            "VALIDATION_ERROR",
            HttpStatus.BAD_REQUEST.value(),
            LocalDateTime.now(),
            path,
            fieldErrors
        );
    }

    /**
     * Factory method to create a ValidationErrorResponse with custom error code.
     */
    public static ValidationErrorResponse of(String message, String errorCode, Map<String, String> fieldErrors, String path) {
        return new ValidationErrorResponse(
            message,
            errorCode,
            HttpStatus.BAD_REQUEST.value(),
            LocalDateTime.now(),
            path,
            fieldErrors
        );
    }
}
