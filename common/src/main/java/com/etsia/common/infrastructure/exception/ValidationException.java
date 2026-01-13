package com.etsia.common.infrastructure.exception;

import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.Map;

/**
 * Exception thrown when input validation fails.
 * Returns HTTP 400 status with field-level error details.
 */
public class ValidationException extends BaseException {
    
    private static final String DEFAULT_ERROR_CODE = "VALIDATION_ERROR";
    
    private final Map<String, String> fieldErrors;
    
    public ValidationException(String message) {
        super(message, DEFAULT_ERROR_CODE, HttpStatus.BAD_REQUEST);
        this.fieldErrors = Collections.emptyMap();
    }
    
    public ValidationException(String message, Map<String, String> fieldErrors) {
        super(message, DEFAULT_ERROR_CODE, HttpStatus.BAD_REQUEST);
        this.fieldErrors = fieldErrors != null ? Map.copyOf(fieldErrors) : Collections.emptyMap();
    }
    
    public ValidationException(String fieldName, String errorMessage) {
        super(String.format("Validation failed for field '%s': %s", fieldName, errorMessage), 
              DEFAULT_ERROR_CODE, HttpStatus.BAD_REQUEST);
        this.fieldErrors = Map.of(fieldName, errorMessage);
    }
    
    public Map<String, String> getFieldErrors() {
        return fieldErrors;
    }
}
