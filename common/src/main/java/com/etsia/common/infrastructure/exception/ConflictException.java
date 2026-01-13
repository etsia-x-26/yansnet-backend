package com.etsia.common.infrastructure.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a resource conflict occurs.
 * Returns HTTP 409 status.
 */
public class ConflictException extends BaseException {
    
    public ConflictException(String message, String errorCode) {
        super(message, errorCode, HttpStatus.CONFLICT);
    }
    
    public static ConflictException forResource(String resourceName, String conflictReason) {
        return new ConflictException(
            String.format("%s conflict: %s", resourceName, conflictReason),
            resourceName.toUpperCase().replace(" ", "_") + "_CONFLICT"
        );
    }
}
