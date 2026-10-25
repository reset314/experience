package com.example.experience.infrastructure.sync.adapter;

import org.springframework.stereotype.Component;

import com.example.experience.domain.datasource.entity.UserDataSource;
import com.example.experience.domain.media.entity.MediaFile;
import com.example.experience.domain.platform.entity.Platform;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class SyncAdapterFactoryImpl implements SyncAdapterFactory {

    @Override
    public String getCredential(UserDataSource dataSource, String key) {
        return null;
    }

    @Override
    public String getSyncConfig(UserDataSource dataSource) {
        return dataSource != null ? dataSource.getSyncConfig() : null;
    }

    @Override
    public String getPlatformConfig(Platform platform) {
        return platform != null ? platform.getConfig() : null;
    }

    @Override
    public MediaFile downloadMedia(UserDataSource dataSource, String url) {
        return null;
    }

    @Override
    public WriteResult upsertEntity(FetchContext ctx, String type, String snapshotJson) {
        return null;
    }

    @Override
    public WriteResult upsertEvent(FetchContext ctx, String eventType, String payloadJson) {
        return null;
    }

    @Override
    public void deleteEntity(FetchContext ctx, String type, String identityKey) {
    }

    @Override
    public void deleteEvent(FetchContext ctx, String eventType, String dedupKey) {
    }

    @Override
    public void log(String message) {
        log.info("{}", message);
    }
}
