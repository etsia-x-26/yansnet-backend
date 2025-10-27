package com.etsia.auth.infrastructure.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "Réponse d'authentification contenant le token JWT et les informations de l'utilisateur")
public class AuthResponse {

    @Schema(
            description = "Identifiant unique de l'utilisateur",
            example = "42"
    )
    private Integer userId;

    @Schema(
            description = "Adresse email de l'utilisateur",
            example = "john.doe@etsia.com"
    )
    private String email;

    @Schema(
            description = "Token JWT pour l'authentification des requêtes suivantes",
            example = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJxN..."
    )
    private String accessToken;

    @Schema(
            description = "Type de token (toujours 'Bearer')",
            example = "Bearer",
            defaultValue = "Bearer"
    )
    private String tokenType = "Bearer";

    public AuthResponse(Integer userId, String email, String accessToken, String tokenType) {
        this.userId = userId;
        this.email = email;
        this.accessToken = accessToken;
        this.tokenType = tokenType;
    }
}
