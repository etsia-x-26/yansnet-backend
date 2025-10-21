package com.etsia.auth.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate keycloakRestTemplate() {
        // Configuration simple avec SimpleClientHttpRequestFactory
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(2000);    // 2 secondes - Timeout pour établir la connexion TCP
        factory.setReadTimeout(10000);      // 10 secondes - Timeout pour recevoir la réponse (augmenté pour Keycloak)

        return new RestTemplate(factory);
    }
}
