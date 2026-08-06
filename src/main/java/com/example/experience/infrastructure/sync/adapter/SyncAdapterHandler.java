package com.example.experience.infrastructure.sync.adapter;

public interface SyncAdapterHandler {

    /**
     * 从数据源拉取事件。
     */
    SyncResult fetchEvents(FetchContext ctx);
}
