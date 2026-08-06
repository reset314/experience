package com.example.experience.infrastructure.sync.adapter;

import com.example.experience.domain.datasource.entity.UserDataSource;
import com.example.experience.domain.event.entity.Event;
import com.example.experience.domain.media.entity.MediaFile;
import com.example.experience.domain.platform.entity.Platform;

/**
 * 同步适配器工厂接口。
 * 为内置适配器和用户上传的适配器提供统一的辅助函数（HTTP 客户端、JSON 映射、
 * 凭据读取、配置读取、媒体下载、事件保存、日志记录）。
 * 工厂本身是无状态的 Spring Bean，需要上下文的方法通过参数传入。
 */
public interface SyncAdapterFactory {

    Object httpClient();

    Object jsonMapper();

    String getCredential(UserDataSource dataSource, String key);

    String getSyncConfig(UserDataSource dataSource);

    String getPlatformConfig(Platform platform);

    MediaFile downloadMedia(UserDataSource dataSource, String url);

    Event saveEvent(Event event);

    void log(String message);
}
