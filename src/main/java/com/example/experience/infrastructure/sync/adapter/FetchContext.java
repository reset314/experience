package com.example.experience.infrastructure.sync.adapter;

import com.example.experience.domain.datasource.entity.UserDataSource;
import com.example.experience.domain.platform.entity.Platform;

/**
 * 一次Sync的上下文,用于传递一次Sync的所有信息即ctx
 */
public interface FetchContext {

    SyncAdapterFactory factory();

    UserDataSource dataSource();

    Platform platform();

    AdapterManifest manifest();

    String syncLogId();

    SyncTrigger trigger();

    SyncStats stats();
}
