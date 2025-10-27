package com.etsia.auth.infrastructure.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Requête de connexion d'un utilisateur")
public class LoginRequest {

    @Schema(
            description = "Adresse email de l'utilisateur",
            example = "john.doe@etsia.com",
            required = true
    )
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @Schema(
            description = "Mot de passe de l'utilisateur",
            example = "SecureP@ss123",
            required = true
    )
    @NotBlank(message = "Password is required")
    private String password;
}
