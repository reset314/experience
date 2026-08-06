package com.example.experience.infrastructure.sync.adapter;

import com.example.experience.domain.datasource.entity.UserDataSource;
import com.example.experience.domain.platform.entity.Platform;

public interface FetchContext {

    SyncAdapterFactory factory();

    UserDataSource dataSource();

    Platform platform();
}
