package com.etsia.common.infrastructure.exception;

import org.springframework.http.HttpStatus;

/**
 * Base exception class for all custom business exceptions.
 * Provides consistent error code and HTTP status handling.
 */
public abstract class BaseException extends RuntimeException {
    
    private final String errorCode;
    private final HttpStatus httpStatus;
    
    protected BaseException(String message, String errorCode, HttpStatus httpStatus) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
    
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
