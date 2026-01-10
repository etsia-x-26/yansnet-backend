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
    public KeycloakAuthService(Keycloak keycloak, UserRepository userRepository) {
        this.keycloak = keycloak;
        this.userRepository = userRepository;
        this.restTemplate = new RestTemplate();
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
        AuthUser user = new AuthUser(null, email, "KEYCLOAK_MANAGED");
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

            ResponseEntity<Map> response = restTemplate.exchange(
                    tokenUrl,
                    HttpMethod.POST,
                    request,
                    Map.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return (String) response.getBody().get("access_token");
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

        // Définir le mot de passe après la création
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

}