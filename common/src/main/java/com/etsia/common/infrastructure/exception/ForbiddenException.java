package com.etsia.common.infrastructure.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a user attempts an unauthorized action.
 * Returns HTTP 403 status.
 */
public class ForbiddenException extends BaseException {
    
    private static final String DEFAULT_ERROR_CODE = "ACCESS_DENIED";
    
    public ForbiddenException(String message) {
        super(message, DEFAULT_ERROR_CODE, HttpStatus.FORBIDDEN);
    }
    
    public ForbiddenException(String message, String errorCode) {
        super(message, errorCode, HttpStatus.FORBIDDEN);
    }
}
