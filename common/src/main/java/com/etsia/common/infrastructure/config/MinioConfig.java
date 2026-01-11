package com.etsia.common.infrastructure.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {

    @Value("${minio.url}")
    private String url;

    @Value("${minio.access-key}")
    private String accessKey;

    @Value("${minio.secret-key}")
    private String secretKey;

    @Bean
    public MinioClient minioClient() {
        String trimmedUrl = url.trim();
        String trimmedAccessKey = accessKey.trim();
        String trimmedSecretKey = secretKey.trim();
        
        System.out.println("Initializing MinIO Client with Endpoint: " + trimmedUrl);
        System.out.println("Access Key Hash: " + trimmedAccessKey.hashCode());
        System.out.println("Secret Key Hash: " + trimmedSecretKey.hashCode());
        
        return MinioClient.builder()
                .endpoint(trimmedUrl)
                .credentials(trimmedAccessKey, trimmedSecretKey)
                .region("us-east-1")
                .build();
    }
}
