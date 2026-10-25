package com.example.experience.infrastructure.sync.adapter;

import com.example.experience.domain.datasource.entity.UserDataSource;
import com.example.experience.domain.entity.entity.EntityRecord;
import com.example.experience.domain.event.entity.Event;
import com.example.experience.domain.media.entity.MediaFile;
import com.example.experience.domain.platform.entity.Platform;

/**
 * 同步适配器工厂接口。
 */
public interface SyncAdapterFactory {

    String getCredential(UserDataSource dataSource, String key);

    String getSyncConfig(UserDataSource dataSource);

    String getPlatformConfig(Platform platform);

    MediaFile downloadMedia(UserDataSource dataSource, String url);

    Event createEvent(Event event);

    Event uploadEvent(Event event);

    Event deleteEvent(Event event);

    EntityRecord createEntityRecord(EntityRecord event);

    EntityRecord uploadEntityRecord(EntityRecord event);

    EntityRecord deleteEntityRecord(EntityRecord event);

    void log(String message);
}
