package com.etsia.auth.infrastructure.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Requête d'inscription d'un nouvel utilisateur")
public class RegisterRequest {

    @Schema(
            description = "Adresse email de l'utilisateur (doit être valide et unique)",
            example = "john.doe@etsia.com",
            required = true
    )
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @Schema(
            description = "Mot de passe de l'utilisateur (minimum 8 caractères)",
            example = "SecureP@ss123",
            required = true,
            minLength = 8
    )
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @Schema(
            description = "Numéro de téléphone de l'utilisateur (optionnel, format international)",
            example = "+221771234567",
            required = false
    )
    private String phoneNumber;
}
