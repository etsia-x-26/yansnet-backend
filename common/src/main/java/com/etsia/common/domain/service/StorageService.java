package com.etsia.common.domain.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

public interface StorageService {
    public String uploadFile(MultipartFile file, String folder);
    void deleteFile(String fileName);
    public String uploadEventImage(MultipartFile file, Integer eventId);
    public String getPresignedUrl(String objectName, int expiryMinutes);
    public boolean fileExists(String objectName);
    public String uploadProfilePicture(MultipartFile file, Integer userId);
}
