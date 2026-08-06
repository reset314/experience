package com.example.experience.infrastructure.sync.adapter;

import org.springframework.stereotype.Component;

import com.example.experience.domain.datasource.entity.UserDataSource;
import com.example.experience.domain.event.entity.Event;
import com.example.experience.domain.media.entity.MediaFile;
import com.example.experience.domain.platform.entity.Platform;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class SyncAdapterFactoryImpl implements SyncAdapterFactory {

    @Override
    public Object httpClient() {
        throw new UnsupportedOperationException("httpClient not implemented yet");
    }

    @Override
    public Object jsonMapper() {
        throw new UnsupportedOperationException("jsonMapper not implemented yet");
    }

    @Override
    public String getCredential(UserDataSource dataSource, String key) {
        throw new UnsupportedOperationException("getCredential not implemented yet");
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
        throw new UnsupportedOperationException("downloadMedia not implemented yet");
    }

    @Override
    public Event saveEvent(Event event) {
        throw new UnsupportedOperationException("saveEvent not implemented yet");
    }

    @Override
    public void log(String message) {
        log.info(message);
    }
}
