package com.etsia.auth.infrastructure.service;

import com.etsia.auth.domain.model.AuthUser;
import com.etsia.auth.domain.repository.UserRepository;
import com.etsia.auth.domain.service.AuthService;
import com.etsia.common.domain.model.sub.Email;
import com.etsia.common.domain.model.sub.PhoneNumber;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

@Service
public class KeycloakAuthService implements AuthService {

    private static final Logger log = LoggerFactory.getLogger(KeycloakAuthService.class);

    private final Keycloak keycloak;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;

    @Value("${keycloak.server-url}")
    private String keycloakServerUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    @Autowired
    public KeycloakAuthService(Keycloak keycloak,
                               UserRepository userRepository,
                               RestTemplate keycloakRestTemplate) {
        this.keycloak = keycloak;
        this.userRepository = userRepository;
        this.restTemplate = keycloakRestTemplate;
    }

    @Override
    public AuthUser authenticate(Email email, String password) {
        // Obtenir le token JWT depuis Keycloak
        String token = getTokenFromKeycloak(email.toString(), password);
        if (token == null) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        // Récupérer l'utilisateur depuis notre base de données
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

    @Override
    public AuthUser register(Email email, String password, PhoneNumber phoneNumber) {
        // Vérifier si l'utilisateur existe déjà
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("User already exists with this email");
        }

        // Créer l'utilisateur dans Keycloak d'abord
        String keycloakUserId = createKeycloakUser(email.toString(), password);

        // Créer l'utilisateur dans notre base de données avec un hash du mot de passe
        AuthUser user = new AuthUser(generateUserId(), email, "KEYCLOAK_MANAGED");
        user.updatePhoneNumber(phoneNumber);

        return userRepository.save(user);
    }

    @Override
    public void logout(Integer userId) {
        // Logique de déconnexion si nécessaire
        // Keycloak gère automatiquement l'expiration des tokens
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

        // Vérifier l'ancien mot de passe avec Keycloak
        if (!isValidCredentials(user.getEmail(), oldPassword)) {
            throw new IllegalArgumentException("Invalid old password");
        }

        // Mettre à jour le mot de passe dans Keycloak
        updateKeycloakPassword(user.getEmail().toString(), newPassword);

        // Mettre à jour dans notre base de données
        user.updatePassword(newPassword);
        userRepository.save(user);
    }

    public String getTokenFromKeycloak(String email, String password) {
        long startTime = System.currentTimeMillis();
        log.info("🔵 Requesting Keycloak token for user: {}", email);

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

            long beforeRequest = System.currentTimeMillis();
            ResponseEntity<Map> response = restTemplate.exchange(
                    tokenUrl,
                    HttpMethod.POST,
                    request,
                    Map.class
            );
            long afterRequest = System.currentTimeMillis();

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                long totalDuration = afterRequest - startTime;
                long requestDuration = afterRequest - beforeRequest;
                log.info("✅ Keycloak token obtained in {}ms (request: {}ms)",
                        totalDuration, requestDuration);
                return (String) response.getBody().get("access_token");
            }

            log.error("❌ Keycloak token request failed with status: {}",
                    response.getStatusCode());
            return null;
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            log.error("❌ Keycloak token request failed after {}ms",
                    duration, e);
            return null;
        }
    }

    private String createKeycloakUser(String email, String password) {
        try {
            // Étape 1 : Obtenir un token admin
            String adminToken = getAdminToken();

            // Étape 2 : Créer l'utilisateur via API REST
            String createUserUrl = keycloakServerUrl + "/admin/realms/" + realm + "/users";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(adminToken);

            // Construire le JSON de l'utilisateur
            Map<String, Object> userPayload = new java.util.HashMap<>();
            userPayload.put("username", email);
            userPayload.put("email", email);
            userPayload.put("enabled", true);
            userPayload.put("emailVerified", true);
            userPayload.put("firstName", "User");
            userPayload.put("lastName", "User");

            // Credentials
            Map<String, Object> credential = new java.util.HashMap<>();
            credential.put("type", "password");
            credential.put("value", password);
            credential.put("temporary", false);
            userPayload.put("credentials", java.util.List.of(credential));

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(userPayload, headers);

            log.info("Creating Keycloak user via REST API: {}", email);
            ResponseEntity<String> response = restTemplate.exchange(
                createUserUrl,
                HttpMethod.POST,
                request,
                String.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                String locationHeader = response.getHeaders().getFirst("Location");
                if (locationHeader != null) {
                    String userId = locationHeader.substring(locationHeader.lastIndexOf('/') + 1);
                    log.info("✅ Keycloak user created successfully: {}", userId);
                    return userId;
                }
            }

            log.error("❌ Failed to create Keycloak user: {}", response.getStatusCode());
            throw new RuntimeException("Failed to create user in Keycloak: " + response.getStatusCode());

        } catch (Exception e) {
            log.error("❌ Error creating Keycloak user", e);
            throw new RuntimeException("Error creating user in Keycloak", e);
        }
    }

    private String getAdminToken() {
        String tokenUrl = keycloakServerUrl + "/realms/master/protocol/openid-connect/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "password");
        map.add("client_id", "admin-cli");
        map.add("username", "admin");
        map.add("password", "admin");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        ResponseEntity<Map> response = restTemplate.exchange(
            tokenUrl,
            HttpMethod.POST,
            request,
            Map.class
        );

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return (String) response.getBody().get("access_token");
        }

        throw new RuntimeException("Failed to get admin token from Keycloak");
    }

    private void updateKeycloakPassword(String email, String newPassword) {
        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();

        // Rechercher l'utilisateur par email
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

    private Integer generateUserId() {
        // Logique pour générer un ID unique
        // Vous pouvez utiliser une séquence ou un UUID
        return (int) (Math.random() * 1000000);
    }
}