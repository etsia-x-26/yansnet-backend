package com.etsia.common.infrastructure.service;

import com.etsia.common.domain.service.StorageService;
import com.etsia.common.infrastructure.exception.BusinessException;
import io.minio.*;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import jakarta.annotation.PostConstruct;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class MinioStorageService implements StorageService {

    private final MinioClient minioClient;

    @Value("${minio.url:http://localhost:9002}")
    private String minioUrl;

    @Value("${minio.bucket-name:yansnet-media}")
    private String bucketName;

    @Value("${minio.public-url:http://localhost:9002}")
    private String publicUrl;
    
    // Note: Initialization is now handled in MinioConfig

    public String uploadFile(MultipartFile file, String folder) {
        try {
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename != null && originalFilename.contains(".")
                    ? originalFilename.substring(originalFilename.lastIndexOf("."))
                    : "";

            // Generate unique filename
            String objectName = folder + "/" + UUID.randomUUID() + extension;

            // Upload file
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );

            // Return the full URL
            return publicUrl + "/" + bucketName + "/" + objectName;

        } catch (Exception e) {
            throw new BusinessException("Failed to upload file", "FILE_UPLOAD_FAILED");
        }
    }


    /**
     * Upload an image specifically for events (cover images)
     */
    public String uploadEventImage(MultipartFile file, UUID eventId) {
        return uploadFile(file, "events/" + eventId);
    }

    /**
     * Upload a user profile picture
     */
    public String uploadProfilePicture(MultipartFile file, UUID userId) {
        return uploadFile(file, "users/" + userId);
    }

    /**
     * Get a presigned URL for temporary access
     */
    public String getPresignedUrl(String objectName, int expiryMinutes) {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(objectName)
                            .expiry(expiryMinutes, TimeUnit.MINUTES)
                            .build()
            );
        } catch (Exception e) {
            throw new BusinessException("Failed to generate presigned URL", "PRESIGNED_URL_FAILED");
        }
    }

    public String uploadEventImage(MultipartFile file, Integer eventId) {
        return uploadFile(file, "events/" + eventId);
    }

    /**
     * Upload a user profile picture
     */
    public String uploadProfilePicture(MultipartFile file, Integer userId) {
        return uploadFile(file, "users/" + userId);
    }

    /**
     * Delete a file from MinIO
     */
    public void deleteFile(String objectName) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );
        } catch (Exception e) {
            throw new BusinessException("Failed to delete file", "FILE_DELETE_FAILED");
        }
    }

    /**
     * Check if a file exists
     */
    public boolean fileExists(String objectName) {
        try {
            minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
