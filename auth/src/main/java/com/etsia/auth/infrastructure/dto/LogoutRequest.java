package com.etsia.auth.infrastructure.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Requête de déconnexion d'un utilisateur")
public class LogoutRequest {

    @Schema(
            description = "Identifiant unique de l'utilisateur à déconnecter",
            example = "42",
            required = true
    )
    @NotNull(message = "User ID is required")
    private Integer userId;
}
