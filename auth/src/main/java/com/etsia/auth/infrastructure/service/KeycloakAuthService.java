package com.etsia.auth.infrastructure.service;

import com.etsia.auth.domain.model.AuthUser;
import com.etsia.auth.domain.repository.UserRepository;
import com.etsia.auth.domain.service.AuthService;
import com.etsia.auth.infrastructure.dto.TokenResponse;
import com.etsia.common.domain.model.sub.Email;
import com.etsia.common.domain.model.sub.PhoneNumber;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class KeycloakAuthService implements AuthService {

    private final Keycloak keycloak;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;
    private final TokenCacheService tokenCacheService;

    @Value("${keycloak.server-url}")
    private String keycloakServerUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    @Value("${auth.access-token-expiration:900000}")
    private long accessTokenExpiration;

    @Value("${auth.refresh-token-expiration:604800000}")
    private long refreshTokenExpiration;

    @Autowired
    public KeycloakAuthService(Keycloak keycloak, UserRepository userRepository, TokenCacheService tokenCacheService) {
        this.keycloak = keycloak;
        this.userRepository = userRepository;
        this.tokenCacheService = tokenCacheService;
        this.restTemplate = new RestTemplate();
    }

    @Override
    public AuthUser authenticate(Email email, String password) {
        String token = getTokenFromKeycloak(email.toString(), password);
        if (token == null) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        Optional<AuthUser> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User not found in local database");
        }

        AuthUser user = userOpt.get();
        if (!user.canAuthenticate()) {
            throw new IllegalStateException("User account is inactive or blocked");
        }

        return user;
    }

    public TokenResponse authenticateWithTokens(Email email, String password) {
        Map<String, Object> tokenData = getFullTokenFromKeycloak(email.toString(), password);
        if (tokenData == null) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        Optional<AuthUser> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User not found in local database");
        }

        AuthUser user = userOpt.get();
        if (!user.canAuthenticate()) {
            throw new IllegalStateException("User account is inactive or blocked");
        }

        String accessToken = (String) tokenData.get("access_token");
        String refreshToken = generateRefreshToken();
        
        // Store refresh token in Redis
        tokenCacheService.storeRefreshToken(refreshToken, user.getUserId(), refreshTokenExpiration);

        return TokenResponse.builder()
                .userId(user.getUserId())
                .email(user.getEmail().toString())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(accessTokenExpiration / 1000)
                .refreshExpiresIn(refreshTokenExpiration / 1000)
                .build();
    }

    public TokenResponse refreshAccessToken(String refreshToken) {
        Integer userId = tokenCacheService.getUserIdFromRefreshToken(refreshToken);
        if (userId == null) {
            throw new IllegalArgumentException("Invalid or expired refresh token");
        }

        Optional<AuthUser> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }

        AuthUser user = userOpt.get();
        if (!user.canAuthenticate()) {
            throw new IllegalStateException("User account is inactive or blocked");
        }

        // Invalidate old refresh token (rotation)
        tokenCacheService.invalidateRefreshToken(refreshToken);

        // Generate new tokens
        String newRefreshToken = generateRefreshToken();
        String newAccessToken = getServiceAccountToken(user.getEmail().toString());

        // Store new refresh token
        tokenCacheService.storeRefreshToken(newRefreshToken, userId, refreshTokenExpiration);

        return TokenResponse.builder()
                .userId(user.getUserId())
                .email(user.getEmail().toString())
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .tokenType("Bearer")
                .expiresIn(accessTokenExpiration / 1000)
                .refreshExpiresIn(refreshTokenExpiration / 1000)
                .build();
    }

    private String generateRefreshToken() {
        return UUID.randomUUID().toString() + "-" + UUID.randomUUID().toString();
    }

    private String getServiceAccountToken(String userEmail) {
        // For refresh, we use client credentials + impersonation or service account
        // This is a simplified version - in production you might want to use Keycloak's token exchange
        try {
            String tokenUrl = keycloakServerUrl + "/realms/" + realm + "/protocol/openid-connect/token";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("grant_type", "client_credentials");
            map.add("client_id", clientId);
            map.add("client_secret", clientSecret);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

            ResponseEntity<Map> response = restTemplate.exchange(tokenUrl, HttpMethod.POST, request, Map.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return (String) response.getBody().get("access_token");
            }
            return null;
        } catch (Exception e) {
            System.out.println("Failed to get service account token: " + e.getMessage());
            return null;
        }
    }

    @Override
    public AuthUser register(Email email, String name, String username, String password, PhoneNumber phoneNumber) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("User already exists with this email");
        }

        String keycloakUserId = createKeycloakUser(email.toString(), password);

        AuthUser user = new AuthUser(null, email, name, username, "KEYCLOAK_MANAGED");
        user.updatePhoneNumber(phoneNumber);

        return userRepository.save(user);
    }

    @Override
    public void logout(Integer userId) {
        tokenCacheService.invalidateAllUserTokens(userId);
    }

    @Override
    public boolean isValidCredentials(Email email, String password) {
        String token = getTokenFromKeycloak(email.toString(), password);
        return token != null;
    }

    @Override
    public void changePassword(Integer userId, String oldPassword, String newPassword) {
        Optional<AuthUser> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }

        AuthUser user = userOpt.get();

        if (!isValidCredentials(user.getEmail(), oldPassword)) {
            throw new IllegalArgumentException("Invalid old password");
        }

        updateKeycloakPassword(user.getEmail().toString(), newPassword);

        user.updatePassword(newPassword);
        userRepository.save(user);
        
        // Invalidate all tokens after password change
        tokenCacheService.invalidateAllUserTokens(userId);
    }

    public String getTokenFromKeycloak(String email, String password) {
        Map<String, Object> tokenData = getFullTokenFromKeycloak(email, password);
        return tokenData != null ? (String) tokenData.get("access_token") : null;
    }

    private Map<String, Object> getFullTokenFromKeycloak(String email, String password) {
        try {
            String tokenUrl = keycloakServerUrl + "/realms/" + realm + "/protocol/openid-connect/token";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("grant_type", "password");
            map.add("client_id", clientId);
            map.add("client_secret", clientSecret);
            map.add("username", email);
            map.add("password", password);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

            ResponseEntity<Map> response = restTemplate.exchange(tokenUrl, HttpMethod.POST, request, Map.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody();
            }

            System.out.println("Keycloak token request failed with status: " + response.getStatusCode());
            return null;
        } catch (Exception e) {
            System.out.println("Keycloak token request failed with exception: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private String createKeycloakUser(String email, String password) {
        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();

        UserRepresentation user = new UserRepresentation();
        user.setUsername(email);
        user.setEmail(email);
        user.setEnabled(true);
        user.setEmailVerified(true);
        user.setFirstName("User");
        user.setLastName("User");

        jakarta.ws.rs.core.Response response = usersResource.create(user);

        if (response.getStatus() != 201) {
            throw new RuntimeException("Failed to create user in Keycloak: " + response.getStatus());
        }

        String locationHeader = response.getHeaderString("Location");
        String userId = locationHeader.substring(locationHeader.lastIndexOf('/') + 1);

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        credential.setTemporary(false);

        usersResource.get(userId).resetPassword(credential);

        return userId;
    }

    private void updateKeycloakPassword(String email, String newPassword) {
        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();

        var users = usersResource.search(email);
        if (users.isEmpty()) {
            throw new IllegalArgumentException("User not found in Keycloak");
        }

        UserRepresentation user = users.get(0);

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(newPassword);
        credential.setTemporary(false);

        usersResource.get(user.getId()).resetPassword(credential);
    }
}