package com.etsia.common.infrastructure.controller;

import com.etsia.common.domain.service.StorageService;
import com.etsia.common.infrastructure.entities.User;
import com.etsia.common.infrastructure.exception.ValidationException;
import com.etsia.common.infrastructure.security.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/media")
@RequiredArgsConstructor
@Tag(name = "Media Management", description = "Endpoints for uploading and managing files in MinIO storage")
public class MediaController {

    private final StorageService storageService;




    @PostMapping(value = "/upload/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload profile picture", description = "Uploads a profile picture for the current user")
    public ResponseEntity<UploadResponse> uploadProfilePicture(
            @Parameter(hidden = true) @CurrentUser User user,
            @RequestParam("file") MultipartFile file) {

        validateImageFile(file);
        String url = storageService.uploadProfilePicture(file, user.getId());
        return ResponseEntity.ok(new UploadResponse(url, file.getOriginalFilename(), file.getSize()));
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload generic file", description = "Uploads a file to a specified folder")
    public ResponseEntity<UploadResponse> uploadFile(
            @Parameter(hidden = true) @CurrentUser User user,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "folder", defaultValue = "uploads") String folder) {

        String url = storageService.uploadFile(file, folder);
        return ResponseEntity.ok(new UploadResponse(url, file.getOriginalFilename(), file.getSize()));
    }

    private void validateImageFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new ValidationException("file", "File is empty");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new ValidationException("file", "Only image files are allowed");
        }

        // Max 10MB
        if (file.getSize() > 10 * 1024 * 1024) {
            throw new ValidationException("file", "File size exceeds maximum limit of 10MB");
        }
    }

    public record UploadResponse(String url, String filename, long size) {}
}
