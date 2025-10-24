package com.etsia.auth.infrastructure.service;

import com.etsia.auth.domain.model.AuthUser;
import com.etsia.auth.domain.repository.UserRepository;
import com.etsia.auth.domain.service.AuthService;
import com.etsia.auth.infrastructure.repository.JpaKeycloakUserMappingRepository;
import com.etsia.auth.infrastructure.repository.KeycloakUserMappingEntity;
import com.etsia.common.domain.model.sub.Email;
import com.etsia.common.domain.model.sub.PhoneNumber;
import jakarta.ws.rs.core.Response;
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
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Map;
import java.util.Optional;

@Service
public class KeycloakAuthService implements AuthService {

    private static final Logger log = LoggerFactory.getLogger(KeycloakAuthService.class);

    private final Keycloak keycloak;
    private final UserRepository userRepository;
    private final JpaKeycloakUserMappingRepository keycloakMappingRepository;

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
                               JpaKeycloakUserMappingRepository keycloakMappingRepository) {
        this.keycloak = keycloak;
        this.userRepository = userRepository;
        this.keycloakMappingRepository = keycloakMappingRepository;
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
    @org.springframework.transaction.annotation.Transactional
    public AuthUser register(Email email, String password, PhoneNumber phoneNumber) {
        log.info("🔵 Starting registration for user: {}", email);
        long startTime = System.currentTimeMillis();

        // Vérifier si l'utilisateur existe déjà
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("User already exists with this email");
        }

        // 1. Créer l'utilisateur dans la base de données D'ABORD (pour obtenir l'ID généré)
        AuthUser user = new AuthUser(null, email, "KEYCLOAK_MANAGED");
        user.updatePhoneNumber(phoneNumber);

        AuthUser savedUser = userRepository.save(user);
        log.info("✅ Database user created with ID: {}", savedUser.getUserId());

        // 2. Créer l'utilisateur dans Keycloak ENSUITE
        try {
            String keycloakUserId = createKeycloakUser(email.toString(), password);
            log.info("✅ Keycloak user created with ID: {}", keycloakUserId);

            // 3. Sauvegarder le mapping entre l'ID interne et l'UUID Keycloak
            KeycloakUserMappingEntity mapping = new KeycloakUserMappingEntity(
                savedUser.getUserId(),
                keycloakUserId
            );
            keycloakMappingRepository.save(mapping);
            log.info("✅ Keycloak mapping saved: {} → {}", savedUser.getUserId(), keycloakUserId);

            long duration = System.currentTimeMillis() - startTime;
            log.info("✅ Registration completed in {}ms", duration);

            return savedUser;
        } catch (Exception e) {
            // Si la création Keycloak échoue, supprimer l'utilisateur de la base de données (rollback)
            log.error("❌ Keycloak user creation failed, rolling back database user", e);
            userRepository.delete(savedUser);
            throw new RuntimeException("Failed to create user in Keycloak: " + e.getMessage(), e);
        }
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

        // Récupérer le UUID Keycloak depuis le mapping
        Optional<KeycloakUserMappingEntity> mappingOpt = keycloakMappingRepository.findByInternalUserId(userId);
        if (mappingOpt.isEmpty()) {
            throw new IllegalStateException("Keycloak mapping not found for user " + userId);
        }

        String keycloakUserId = mappingOpt.get().getKeycloakUserId();

        // Mettre à jour le mot de passe dans Keycloak
        updateKeycloakPassword(keycloakUserId, newPassword);

        // Mettre à jour dans notre base de données
        user.updatePassword(newPassword);
        userRepository.save(user);
    }

    public String getTokenFromKeycloak(String email, String password) {
        long startTime = System.currentTimeMillis();
        log.info("🔵 Requesting Keycloak token for user: {}", email);

        try {
            // Créer un RestTemplate simple avec Connection: close pour éviter les problèmes keep-alive
            SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory() {
                @Override
                protected void prepareConnection(HttpURLConnection connection, String httpMethod) throws IOException {
                    super.prepareConnection(connection, httpMethod);
                    connection.setRequestProperty("Connection", "close");
                }
            };
            factory.setConnectTimeout(60000);
            factory.setReadTimeout(60000);
            RestTemplate simpleRestTemplate = new RestTemplate(factory);

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
            ResponseEntity<Map> response = simpleRestTemplate.exchange(
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
        } catch (org.springframework.web.client.HttpClientErrorException e) {
            long duration = System.currentTimeMillis() - startTime;
            String tokenUrl = keycloakServerUrl + "/realms/" + realm + "/protocol/openid-connect/token";
            log.error("❌ Keycloak returned {}. Response body: {}",
                e.getStatusCode(), e.getResponseBodyAsString());
            log.error("❌ Request details - URL: {}, Client ID: {}, Username: {}",
                tokenUrl, clientId, email);
            log.error("❌ Token request failed after {}ms", duration);
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
            log.info("Creating Keycloak user via SDK: {}", email);

            // Obtenir l'accès au realm
            RealmResource realmResource = keycloak.realm(realm);
            UsersResource usersResource = realmResource.users();

            // Créer la représentation de l'utilisateur
            UserRepresentation newUser = new UserRepresentation();
            newUser.setEnabled(true);
            newUser.setUsername(email);
            newUser.setEmail(email);
            newUser.setEmailVerified(true);
            newUser.setFirstName("User");
            newUser.setLastName("User");

            // Créer le credential (mot de passe)
            CredentialRepresentation credential = new CredentialRepresentation();
            credential.setType(CredentialRepresentation.PASSWORD);
            credential.setValue(password);
            credential.setTemporary(false);
            newUser.setCredentials(java.util.List.of(credential));

            // Créer l'utilisateur dans Keycloak
            Response response = usersResource.create(newUser);

            int status = response.getStatus();
            log.info("Keycloak user creation response status: {}", status);

            if (status == 201) {
                // Récupérer l'ID de l'utilisateur créé depuis le header Location
                String locationHeader = response.getHeaderString("Location");
                if (locationHeader != null) {
                    String userId = locationHeader.substring(locationHeader.lastIndexOf('/') + 1);
                    log.info("✅ Keycloak user created successfully with ID: {}", userId);
                    response.close();
                    return userId;
                }
            } else if (status == 409) {
                log.error("❌ User already exists in Keycloak: {}", email);
                response.close();
                throw new IllegalArgumentException("User already exists in Keycloak");
            }

            String errorMessage = response.readEntity(String.class);
            response.close();
            log.error("❌ Failed to create Keycloak user. Status: {}, Error: {}", status, errorMessage);
            throw new RuntimeException("Failed to create user in Keycloak. Status: " + status);

        } catch (Exception e) {
            log.error("❌ Error creating Keycloak user: {}", email, e);
            throw new RuntimeException("Error creating user in Keycloak: " + e.getMessage(), e);
        }
    }


    private void updateKeycloakPassword(String keycloakUserId, String newPassword) {
        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(newPassword);
        credential.setTemporary(false);

        // Utiliser directement le UUID Keycloak (plus efficace que la recherche par email)
        usersResource.get(keycloakUserId).resetPassword(credential);
        log.info("✅ Password updated in Keycloak for user: {}", keycloakUserId);
    }

}