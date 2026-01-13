package com.etsia.common.infrastructure.config;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service; // Added import for @Service

@Configuration
@ConditionalOnProperty(name = "minio.enabled", havingValue = "true", matchIfMissing = true)
public class MinioConfig {

    @Value("${minio.url:http://localhost:9002}")
    private String minioUrl;

    @Value("${minio.access-key:minioadmin}")
    private String accessKey;

    @Value("${minio.secret-key:minioadmin123}")
    private String secretKey;

    @Value("${minio.bucket-name:yansnet-media}")
    private String bucketName;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(minioUrl)
                .credentials(accessKey, secretKey)
                .build();
    }

    @Bean
    public String minioBucketName() {
        return bucketName;
    }

    @PostConstruct
    public void initBucket() {
        // Only initialize bucket in non-test environments
        if (!"test".equals(System.getProperty("spring.profiles.active"))) {
            try {
                MinioClient client = MinioClient.builder()
                        .endpoint(minioUrl)
                        .credentials(accessKey, secretKey)
                        .build();

                boolean found = client.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
                if (!found) {
                    client.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                }

                // Set public read policy
                String policy = """
                    {
                      "Version": "2012-10-17",
                      "Statement": [
                        {
                          "Effect": "Allow",
                          "Principal": { "AWS": ["*"] },
                          "Action": ["s3:GetObject"],
                          "Resource": ["arn:aws:s3:::%s/*"]
                        }
                      ]
                    }
                    """.formatted(bucketName);

                client.setBucketPolicy(
                        io.minio.SetBucketPolicyArgs.builder()
                                .bucket(bucketName)
                                .config(policy)
                                .build()
                );

            } catch (Exception e) {
                // Log warning but don't fail startup - MinIO might not be available
                System.err.println("Warning: Could not initialize MinIO bucket: " + e.getMessage());
            }
        }
    }
}
