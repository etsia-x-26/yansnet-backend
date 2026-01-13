package com.etsia.common.infrastructure.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when authentication fails.
 * Returns HTTP 401 status.
 */
public class AuthenticationException extends BaseException {
    
    private static final String DEFAULT_ERROR_CODE = "AUTHENTICATION_FAILED";
    
    public AuthenticationException(String message) {
        super(message, DEFAULT_ERROR_CODE, HttpStatus.UNAUTHORIZED);
    }
    
    public AuthenticationException(String message, String errorCode) {
        super(message, errorCode, HttpStatus.UNAUTHORIZED);
    }
}
