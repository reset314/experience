package com.example.experience.infrastructure.sync.adapter;

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

    AuthResult initAuth(FetchContext ctx, AuthRequest authRequest);

    AuthResult cheakAuthState(FetchContext ctx, AuthRequest authRequest);

    AuthResult continueAuth(FetchContext ctx, AuthRequest authRequest);

}
