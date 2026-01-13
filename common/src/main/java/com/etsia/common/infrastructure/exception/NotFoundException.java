package com.etsia.common.infrastructure.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a requested resource does not exist.
 * Returns HTTP 404 status.
 */
public class NotFoundException extends BaseException {
    
    public NotFoundException(String message, String errorCode) {
        super(message, errorCode, HttpStatus.NOT_FOUND);
    }
    
    public NotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(
            String.format("%s not found with %s: %s", resourceName, fieldName, fieldValue),
            resourceName.toUpperCase().replace(" ", "_") + "_NOT_FOUND",
            HttpStatus.NOT_FOUND
        );
    }
}
