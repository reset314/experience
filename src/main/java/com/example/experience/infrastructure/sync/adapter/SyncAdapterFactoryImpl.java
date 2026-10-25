package com.example.experience.infrastructure.sync.adapter;

import org.springframework.stereotype.Component;

import com.example.experience.domain.media.entity.MediaFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class SyncAdapterFactoryImpl implements SyncAdapterFactory {

    @Override
    public String getCredential(FetchContext ctx, String key) {
        return null;
    }

    @Override
    public String getSyncConfig(FetchContext ctx) {
        return ctx.dataSource() != null ? ctx.dataSource().getSyncConfig() : null;
    }

    @Override
    public String getPlatformConfig(FetchContext ctx) {
        return ctx.platform() != null ? ctx.platform().getConfig() : null;
    }

    @Override
    public MediaFile downloadMedia(FetchContext ctx, String url) {
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
