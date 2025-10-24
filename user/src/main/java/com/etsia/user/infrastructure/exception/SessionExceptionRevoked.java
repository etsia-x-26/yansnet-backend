package com.etsia.user.infrastructure.exception;

public class SessionExceptionRevoked extends RuntimeException {
    public SessionExceptionRevoked(String message) {
        super(message);
    }
}
