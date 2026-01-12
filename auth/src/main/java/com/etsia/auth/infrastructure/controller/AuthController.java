package com.etsia.auth.infrastructure.controller;

import com.etsia.auth.application.service.AuthApplicationService;
import com.etsia.auth.infrastructure.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
@Tag(name = "Authentication", description = "Endpoints for user login, registration, refresh token, and logout")
public class AuthController {

    @Autowired
    private AuthApplicationService authApplicationService;

    @Operation(summary = "Login a user", description = "Authenticates a user and returns access and refresh tokens")
    @ApiResponse(responseCode = "200", description = "Successfully authenticated")
    @ApiResponse(responseCode = "401", description = "Invalid credentials")
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest request) {
        TokenResponse response = authApplicationService.login(request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Register a new user", description = "Creates a new user account and returns tokens")
    @ApiResponse(responseCode = "200", description = "Successfully registered")
    @ApiResponse(responseCode = "400", description = "Invalid registration data")
    @PostMapping("/register")
    public ResponseEntity<TokenResponse> register(@Valid @RequestBody RegisterRequest request) {
        TokenResponse response = authApplicationService.register(request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Refresh access token", description = "Uses refresh token to get a new access token")
    @ApiResponse(responseCode = "200", description = "Successfully refreshed tokens")
    @ApiResponse(responseCode = "401", description = "Invalid or expired refresh token")
    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        TokenResponse response = authApplicationService.refreshToken(request.getRefreshToken());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Logout a user", description = "Invalidates all user tokens")
    @ApiResponse(responseCode = "200", description = "Successfully logged out")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@Valid @RequestBody LogoutRequest request) {
        authApplicationService.logout(request.getUserId());
        return ResponseEntity.ok().build();
    }
}
