package com.etsia.common.domain.service;

import java.io.InputStream;

public interface StorageService {
    String uploadFile(String fileName, InputStream inputStream, String contentType);
    void deleteFile(String fileName);
    String getFileUrl(String fileName);
}
