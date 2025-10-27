package com.etsia.auth.infrastructure.controller;

import com.etsia.auth.application.service.AuthApplicationService;
import com.etsia.auth.infrastructure.dto.AuthResponse;
import com.etsia.auth.infrastructure.dto.ErrorResponse;
import com.etsia.auth.infrastructure.dto.LoginRequest;
import com.etsia.auth.infrastructure.dto.LogoutRequest;
import com.etsia.auth.infrastructure.dto.RegisterRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(
        name = "Authentication",
        description = "API d'authentification - Gestion de l'inscription, connexion et déconnexion des utilisateurs via Keycloak"
)
public class AuthController {

    @Autowired
    private AuthApplicationService authApplicationService;

    @PostMapping("/login")
    @Operation(
            summary = "Connexion utilisateur",
            description = "Authentifie un utilisateur avec son email et mot de passe. Retourne un token JWT valide pour les requêtes authentifiées."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Connexion réussie - Token JWT généré",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class),
                            examples = @ExampleObject(
                                    name = "Exemple de réponse",
                                    value = """
                                    {
                                      "userId": 42,
                                      "email": "john.doe@etsia.com",
                                      "accessToken": "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJxN...",
                                      "tokenType": "Bearer"
                                    }
                                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Requête invalide - Email ou password manquant/invalide",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "Email invalide",
                                    value = """
                                    {
                                      "timestamp": "2025-10-25T14:30:00",
                                      "status": 400,
                                      "error": "Bad Request",
                                      "message": "Email should be valid",
                                      "path": "/auth/login"
                                    }
                                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Non autorisé - Credentials invalides",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "Credentials invalides",
                                    value = """
                                    {
                                      "timestamp": "2025-10-25T14:30:00",
                                      "status": 401,
                                      "error": "Unauthorized",
                                      "message": "Invalid email or password",
                                      "path": "/auth/login"
                                    }
                                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erreur serveur interne",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "Erreur serveur",
                                    value = """
                                    {
                                      "timestamp": "2025-10-25T14:30:00",
                                      "status": 500,
                                      "error": "Internal Server Error",
                                      "message": "Keycloak service unavailable",
                                      "path": "/auth/login"
                                    }
                                    """
                            )
                    )
            )
    })
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authApplicationService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    @Operation(
            summary = "Inscription nouvel utilisateur",
            description = "Crée un nouveau compte utilisateur dans Keycloak et la base de données locale. L'email doit être unique."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Inscription réussie - Utilisateur créé et token JWT généré",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class),
                            examples = @ExampleObject(
                                    name = "Exemple de réponse",
                                    value = """
                                    {
                                      "userId": 42,
                                      "email": "john.doe@etsia.com",
                                      "accessToken": "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJxN...",
                                      "tokenType": "Bearer"
                                    }
                                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Requête invalide - Validation échouée (email invalide, password trop court, email déjà utilisé)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "Email déjà utilisé",
                                    value = """
                                    {
                                      "timestamp": "2025-10-25T14:30:00",
                                      "status": 400,
                                      "error": "Bad Request",
                                      "message": "Email already exists",
                                      "path": "/auth/register"
                                    }
                                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erreur serveur interne - Impossible de créer l'utilisateur",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "Erreur serveur",
                                    value = """
                                    {
                                      "timestamp": "2025-10-25T14:30:00",
                                      "status": 500,
                                      "error": "Internal Server Error",
                                      "message": "Failed to create user in Keycloak",
                                      "path": "/auth/register"
                                    }
                                    """
                            )
                    )
            )
    })
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authApplicationService.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    @Operation(
            summary = "Déconnexion utilisateur",
            description = "Déconnecte un utilisateur en révoquant sa session Keycloak. Nécessite l'ID de l'utilisateur."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Déconnexion réussie - Session révoquée",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Requête invalide - User ID manquant ou invalide",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "User ID manquant",
                                    value = """
                                    {
                                      "timestamp": "2025-10-25T14:30:00",
                                      "status": 400,
                                      "error": "Bad Request",
                                      "message": "User ID is required",
                                      "path": "/auth/logout"
                                    }
                                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Utilisateur non trouvé",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "Utilisateur non trouvé",
                                    value = """
                                    {
                                      "timestamp": "2025-10-25T14:30:00",
                                      "status": 404,
                                      "error": "Not Found",
                                      "message": "User not found",
                                      "path": "/auth/logout"
                                    }
                                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erreur serveur interne",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "Erreur serveur",
                                    value = """
                                    {
                                      "timestamp": "2025-10-25T14:30:00",
                                      "status": 500,
                                      "error": "Internal Server Error",
                                      "message": "Failed to logout from Keycloak",
                                      "path": "/auth/logout"
                                    }
                                    """
                            )
                    )
            )
    })
    public ResponseEntity<Void> logout(@Valid @RequestBody LogoutRequest request) {
        authApplicationService.logout(request.getUserId());
        return ResponseEntity.ok().build();
    }
}
