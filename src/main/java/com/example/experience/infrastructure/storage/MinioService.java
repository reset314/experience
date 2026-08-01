package com.example.experience.infrastructure.storage;

import java.io.InputStream;
import java.io.OutputStream;

import org.springframework.stereotype.Service;

import com.example.experience.common.config.MinioProperties;
import com.example.experience.common.exception.StorageException;

import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.StatObjectArgs;
import io.minio.http.Method;
import io.minio.errors.ErrorResponseException;
import org.apache.commons.io.IOUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.example.experience.common.utils.MimeTypeUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class MinioService implements IMinioService {

    private final MinioClient minioClient;
    private final MinioProperties minioProperties;

    private String guessContentType(String objectKey){
        return MimeTypeUtils.GuessContentType(objectKey);
    }

    public void ensureBucketExists() {
        try {
            String bucket = minioProperties.getBucket();
            boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
            if (!exists) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
                log.info("Created MinIO bucket: {}", bucket);
            }
        } catch (Exception e) {
            log.error("MinIO bucket initialization failed", e);
            throw new StorageException("MinIO bucket initialization failed", e);
        }
    }

    @Override
    public String uploadFile(String bucket, String objectKey, InputStream inputStream, long size) {
        try {
            String contentType = guessContentType(objectKey);

            ObjectWriteResponse response = minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectKey)
                            .stream(inputStream, size, -1)
                            .contentType(contentType)
                            .build()
            );
            log.info("File uploaded successfully: {}/{}, ETag: {}", bucket, objectKey, response.etag());
            return response.etag();
        } catch (Exception e) {
            log.error("Failed to upload file: {}/{}", bucket, objectKey, e);
            throw new StorageException("File upload failed", e);
        }
    }

    @Override
    public void downloadFile(String bucket, String objectKey, OutputStream outputStream) {
        try (InputStream is = minioClient.getObject(
                GetObjectArgs.builder().bucket(bucket).object(objectKey).build())) {
            IOUtils.copyLarge(is, outputStream);
        } catch (Exception e) {
            throw new StorageException("File download failed: " + objectKey, e);
        }
    }

    @Override
    public String getPresignedUrl(String bucket, String objectKey, int expirySeconds) {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .bucket(bucket)
                            .object(objectKey)
                            .method(Method.GET)
                            .expiry(expirySeconds)
                            .build()
            );
        } catch (Exception e) {
            throw new StorageException("Generate presigned URL failed", e);
        }
    }
    
    @Override
    public void deleteFile(String bucket, String objectKey){
        try {
            RemoveObjectArgs args = RemoveObjectArgs.builder()
                    .bucket(bucket)
                    .object(objectKey)
                    .build();
            minioClient.removeObject(args);
        } catch (Exception e){
            throw new StorageException("Failed detele file: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean fileExists(String bucket, String objectKey) {
        try {
            minioClient.statObject(
                StatObjectArgs.builder()
                    .bucket(bucket)
                    .object(objectKey)
                    .build()
            );
            return true;
        } catch (ErrorResponseException e) {
            String errorCode = e.errorResponse().code();
            if ("NoSuchKey".equals(errorCode) || "NoSuchBucket".equals(errorCode)) {
                return false;
            }
            throw new StorageException("Failed to check file existence", e);
        } catch (Exception e) {
            throw new StorageException("Failed to check file existence", e);
        }
    }
}