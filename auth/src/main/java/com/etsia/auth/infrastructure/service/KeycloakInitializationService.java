package com.etsia.auth.infrastructure.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.RealmRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class KeycloakInitializationService {

    private final Keycloak keycloak;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    @PostConstruct
    public void init() {
        try {
            ensureRealmExists();
            ensureClientExists();
        } catch (Exception e) {
            log.error("Failed to initialize Keycloak", e);
        }
    }

    private void ensureRealmExists() {
        List<RealmRepresentation> realms = keycloak.realms().findAll();
        boolean exists = realms.stream().anyMatch(r -> r.getRealm().equals(realm));

        if (!exists) {
            log.info("Creating Keycloak realm: {}", realm);
            RealmRepresentation newRealm = new RealmRepresentation();
            newRealm.setRealm(realm);
            newRealm.setEnabled(true);
            newRealm.setRegistrationAllowed(true);
            keycloak.realms().create(newRealm);
            log.info("Realm '{}' created successfully.", realm);
        } else {
            log.debug("Realm '{}' already exists.", realm);
        }
    }

    private void ensureClientExists() {
        RealmResource realmResource = keycloak.realm(realm);
        List<ClientRepresentation> clients = realmResource.clients().findAll();
        boolean exists = clients.stream().anyMatch(c -> c.getClientId().equals(clientId));

        if (!exists) {
            log.info("Creating Keycloak client: {}", clientId);
            ClientRepresentation client = new ClientRepresentation();
            client.setClientId(clientId);
            client.setSecret(clientSecret);
            client.setServiceAccountsEnabled(true);
            client.setDirectAccessGrantsEnabled(true);
            client.setPublicClient(false);
            client.setEnabled(true);
            client.setRedirectUris(Collections.singletonList("*"));
            client.setWebOrigins(Collections.singletonList("*"));
            
            realmResource.clients().create(client);
            log.info("Client '{}' created successfully in realm '{}'.", clientId, realm);
        } else {
            log.debug("Client '{}' already exists.", clientId);
        }
    }
}
