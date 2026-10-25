package com.example.experience.infrastructure.sync.adapter;

import com.example.experience.infrastructure.sync.adapter.auth.AuthCompleteRequest;
import com.example.experience.infrastructure.sync.adapter.auth.AuthCompleteResponse;
import com.example.experience.infrastructure.sync.adapter.auth.AuthInitRequest;
import com.example.experience.infrastructure.sync.adapter.auth.AuthInitResponse;
import com.example.experience.infrastructure.sync.adapter.auth.AuthStatusRequest;
import com.example.experience.infrastructure.sync.adapter.auth.AuthStatusResponse;

public interface SyncAdapterHandler {

    AdapterManifest manifest();

    /**
     * 从数据源拉取事件。
     */
    void fetchEvents(FetchContext ctx);

    /**
     * 上传一个事件
     */
    void uploadEvent(FetchContext ctx);

}
