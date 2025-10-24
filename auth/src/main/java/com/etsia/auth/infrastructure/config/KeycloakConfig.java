package com.etsia.auth.infrastructure.config;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class KeycloakConfig {

    private static final Logger log = LoggerFactory.getLogger(KeycloakConfig.class);

    @Value("${keycloak.server-url}")
    private String serverUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.admin-client-id}")
    private String adminClientId;

    @Value("${keycloak.admin-username}")
    private String adminUsername;

    @Value("${keycloak.admin-password}")
    private String adminPassword;

    @Bean
    public Keycloak keycloak() {
        log.info("Initializing Keycloak Admin Client...");
        log.info("Server URL: {}", serverUrl);
        log.info("Realm: {}", realm);
        log.info("Admin Client ID: {}", adminClientId);

        // Configuration du client HTTP avec timeouts adaptés et connection pooling désactivé
        // Désactivation du keep-alive pour éviter les problèmes WSL2/Docker
        Client resteasyClient = ((ResteasyClientBuilder) ClientBuilder.newBuilder())
                .connectionPoolSize(1)  // Pool minimal
                .connectTimeout(60, TimeUnit.SECONDS)  // 60s pour établir connexion
                .readTimeout(60, TimeUnit.SECONDS)  // 60s pour lire la réponse
                .disableAutomaticRetries()  // Pas de retry automatique
                .property("http.connection.reuse", false)  // Désactiver la réutilisation de connexion
                .property("http.keepAlive", false)  // Désactiver keep-alive
                .build();

        Keycloak keycloakClient = KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm("master")  // Admin realm
                .clientId(adminClientId)
                .username(adminUsername)
                .password(adminPassword)
                .resteasyClient(resteasyClient)
                .build();

        log.info("Keycloak Admin Client initialized successfully");
        return keycloakClient;
    }
}