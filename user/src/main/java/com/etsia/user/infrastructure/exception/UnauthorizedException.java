package com.etsia.user.infrastructure.exception;

public class UnauthorizedException extends BaseException {
    public UnauthorizedException(String message) {
        super(message);
    }

    //public UnauthorizedException() {
        //super("Opération non autorisée");
    //}
}
