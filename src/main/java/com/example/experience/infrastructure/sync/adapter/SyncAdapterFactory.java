package com.example.experience.infrastructure.sync.adapter;

import com.example.experience.domain.datasource.entity.UserDataSource;
import com.example.experience.domain.media.entity.MediaFile;
import com.example.experience.domain.platform.entity.Platform;

public interface SyncAdapterFactory {

    String getCredential(UserDataSource dataSource, String key);

    String getSyncConfig(UserDataSource dataSource);

    String getPlatformConfig(Platform platform);

    MediaFile downloadMedia(UserDataSource dataSource, String url);

    /**type必须是manifest声明了的 */
    WriteResult upsertEntity(FetchContext ctx, String type, String snapshotJson);

    /**
        包含了写入和插入
     */
    WriteResult upsertEvent(FetchContext ctx, String eventType, String payloadJson);

    void deleteEntity(FetchContext ctx, String type, String identityKey);

    void deleteEvent(FetchContext ctx, String eventType, String dedupKey);

    void log(String message);
}