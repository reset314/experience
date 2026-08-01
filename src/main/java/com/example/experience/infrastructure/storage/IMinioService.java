package com.example.experience.infrastructure.storage;

import java.io.InputStream;
import java.io.OutputStream;

public interface IMinioService {
    String uploadFile(String bucket, String objectKey, InputStream inputStream, long size);

    void downloadFile(String bucket, String objectKey, OutputStream outputStream);

    void deleteFile(String bucket, String objectKey);

    boolean fileExists(String bucket, String objectKey);

    String getPresignedUrl(String bucket, String objectKey, int expirySeconds);
}
