package com.etsia.common.infrastructure.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Global exception handler that intercepts all exceptions from controllers
 * and transforms them into standardized error responses.
 * 
 * Requirements: 5.1, 5.2, 5.3, 4.2
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    private static final String INTERNAL_ERROR_CODE = "INTERNAL_ERROR";
    private static final String GENERIC_ERROR_MESSAGE = "An unexpected error occurred. Please try again later.";
    
    // Patterns to detect sensitive information in error messages
    private static final Pattern STACK_TRACE_PATTERN = Pattern.compile("\\bat\\s+[\\w.$]+\\([^)]+\\)");
    private static final Pattern CLASS_NAME_PATTERN = Pattern.compile("\\b[a-z]+\\.[a-z]+\\.[A-Z][\\w]*Exception\\b");
    private static final Pattern PACKAGE_PATH_PATTERN = Pattern.compile("\\b(com|org|net|java|javax)\\.[a-z.]+\\.[A-Z]\\w*\\b");

    /**
     * Handles all custom business exceptions extending BaseException.
     * Maps the exception to the appropriate HTTP response based on the exception's status.
     * 
     * Requirements: 5.2
     */
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleBaseException(BaseException ex, HttpServletRequest request) {
        log.warn("Business exception occurred: {} - {}", ex.getErrorCode(), ex.getMessage());
        
        ErrorResponse response = ErrorResponse.of(
            ex.getMessage(),
            ex.getErrorCode(),
            ex.getHttpStatus(),
            request.getRequestURI()
        );
        
        return ResponseEntity.status(ex.getHttpStatus()).body(response);
    }

    /**
     * Handles ValidationException specifically to return ValidationErrorResponse with field errors.
     * 
     * Requirements: 5.2, 3.1, 3.2
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationException(ValidationException ex, HttpServletRequest request) {
        log.warn("Validation exception occurred: {}", ex.getMessage());
        
        ValidationErrorResponse response = ValidationErrorResponse.of(
            ex.getMessage(),
            ex.getErrorCode(),
            ex.getFieldErrors(),
            request.getRequestURI()
        );
        
        return ResponseEntity.status(ex.getHttpStatus()).body(response);
    }

    /**
     * Handles Spring validation exceptions from @Valid annotations.
     * Transforms MethodArgumentNotValidException into ValidationErrorResponse.
     * 
     * Requirements: 5.3
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        log.warn("Method argument validation failed: {}", ex.getMessage());
        
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            fieldErrors.put(error.getField(), error.getDefaultMessage())
        );
        
        ValidationErrorResponse response = ValidationErrorResponse.of(
            "Validation failed",
            fieldErrors,
            request.getRequestURI()
        );
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Handles all unexpected exceptions.
     * Returns a sanitized error response without exposing internal details.
     * Logs the full exception details server-side.
     * 
     * Requirements: 5.1, 4.1, 4.2, 4.3
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, HttpServletRequest request) {
        // Log full exception details server-side (Requirement 4.2)
        log.error("Unexpected exception occurred at {}: {}", request.getRequestURI(), ex.getMessage(), ex);
        
        // Return sanitized response without internal details (Requirements 4.1, 4.3)
        ErrorResponse response = ErrorResponse.of(
            GENERIC_ERROR_MESSAGE,
            INTERNAL_ERROR_CODE,
            HttpStatus.INTERNAL_SERVER_ERROR,
            request.getRequestURI()
        );
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    /**
     * Sanitizes an error message by removing sensitive information.
     * Used internally to ensure no stack traces, class names, or package paths are exposed.
     * 
     * @param message the original error message
     * @return sanitized message safe for client response
     */
    public static String sanitizeMessage(String message) {
        if (message == null || message.isBlank()) {
            return GENERIC_ERROR_MESSAGE;
        }
        
        String sanitized = message;
        
        // Remove stack trace patterns
        sanitized = STACK_TRACE_PATTERN.matcher(sanitized).replaceAll("");
        
        // Remove class name patterns (e.g., com.example.SomeException)
        sanitized = CLASS_NAME_PATTERN.matcher(sanitized).replaceAll("");
        
        // Remove package path patterns
        sanitized = PACKAGE_PATH_PATTERN.matcher(sanitized).replaceAll("");
        
        // Clean up any resulting whitespace issues
        sanitized = sanitized.replaceAll("\\s+", " ").trim();
        
        // If message is now empty or too short, return generic message
        if (sanitized.isBlank() || sanitized.length() < 3) {
            return GENERIC_ERROR_MESSAGE;
        }
        
        return sanitized;
    }

    /**
     * Checks if a message contains sensitive information.
     * 
     * @param message the message to check
     * @return true if the message contains sensitive patterns
     */
    public static boolean containsSensitiveInfo(String message) {
        if (message == null) {
            return false;
        }
        return STACK_TRACE_PATTERN.matcher(message).find() ||
               CLASS_NAME_PATTERN.matcher(message).find() ||
               PACKAGE_PATH_PATTERN.matcher(message).find();
    }
}
