package com.etsia.interaction.infrastructure.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Intercepte toutes les exceptions non gérées afin de renvoyer un message JSON lisible
 * et d’afficher la stack‑trace dans les logs.
 */
@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handle(Exception e) {
        e.printStackTrace(); // visible dans la console
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(e.getClass().getSimpleName() + ": " + e.getMessage());
    }
}