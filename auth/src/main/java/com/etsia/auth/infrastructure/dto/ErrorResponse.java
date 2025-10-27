package com.etsia.auth.infrastructure.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Format de réponse pour les erreurs API")
public class ErrorResponse {

    @Schema(description = "Timestamp de l'erreur", example = "2025-10-25T14:30:00")
    private LocalDateTime timestamp;

    @Schema(description = "Code de statut HTTP", example = "400")
    private int status;

    @Schema(description = "Type d'erreur", example = "Bad Request")
    private String error;

    @Schema(description = "Message d'erreur détaillé", example = "Email should be valid")
    private String message;

    @Schema(description = "Chemin de la requête qui a causé l'erreur", example = "/auth/register")
    private String path;

    public ErrorResponse(int status, String error, String message, String path) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }
}
