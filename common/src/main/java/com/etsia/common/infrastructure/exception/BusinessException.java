package com.etsia.common.infrastructure.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a business rule is violated.
 * Returns HTTP 422 status.
 */
public class BusinessException extends BaseException {
    
    private static final String DEFAULT_ERROR_CODE_PREFIX = "BUSINESS_RULE_";
    
    public BusinessException(String message, String ruleName) {
        super(message, DEFAULT_ERROR_CODE_PREFIX + ruleName.toUpperCase().replace(" ", "_"), 
              HttpStatus.UNPROCESSABLE_ENTITY);
    }
    
    public BusinessException(String message) {
        super(message, DEFAULT_ERROR_CODE_PREFIX + "VIOLATION", HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
