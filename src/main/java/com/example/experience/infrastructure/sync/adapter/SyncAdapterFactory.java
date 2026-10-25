package com.example.experience.infrastructure.sync.adapter;

import com.example.experience.domain.media.entity.MediaFile;

public interface SyncAdapterFactory {

    String getCredential(FetchContext ctx, String key);

    String getSyncConfig(FetchContext ctx);

    String getPlatformConfig(FetchContext ctx);

    MediaFile downloadMedia(FetchContext ctx, String url);

    WriteResult upsertEntity(FetchContext ctx, String type, String snapshotJson);

    /**
        包含了写入和插入
     */
    WriteResult upsertEvent(FetchContext ctx, String eventType, String payloadJson);

    void deleteEntity(FetchContext ctx, String type, String identityKey);

    void deleteEvent(FetchContext ctx, String eventType, String dedupKey);

    void log(String message);
}