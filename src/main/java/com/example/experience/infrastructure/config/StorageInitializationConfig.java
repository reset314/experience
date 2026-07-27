package com.example.experience.infrastructure.config;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.example.experience.infrastructure.storage.MinioService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StorageInitializationConfig {

    private final MinioService minioService;

    @EventListener(ApplicationReadyEvent.class)
    public void initStorage() {
        minioService.ensureBucketExists();
    }
}
