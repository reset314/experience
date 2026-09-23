package com.example.experience.infrastructure.sync.adapter;

import com.example.experience.infrastructure.sync.adapter.auth.AuthCompleteRequest;
import com.example.experience.infrastructure.sync.adapter.auth.AuthCompleteResponse;
import com.example.experience.infrastructure.sync.adapter.auth.AuthInitRequest;
import com.example.experience.infrastructure.sync.adapter.auth.AuthInitResponse;
import com.example.experience.infrastructure.sync.adapter.auth.AuthStatusRequest;
import com.example.experience.infrastructure.sync.adapter.auth.AuthStatusResponse;

public interface SyncAdapterHandler {

    /**
     * 从数据源拉取事件。
     */
    SyncResult fetchEvents(FetchContext ctx);

    /**
     * 上传一个事件
     */
    SyncResult uploadEvent(FetchContext ctx);

    /**
     * 初始化认证流程，例如获取二维码 URL、OAuth 授权链接等。
     */
    default AuthInitResponse initiateAuth(AuthInitRequest request) {
        throw new UnsupportedOperationException("initiateAuth not supported");
    }

    /**
     * 查询认证流程的当前状态，例如二维码是否被扫描、是否被确认等。
     */
    default AuthStatusResponse checkAuthStatus(AuthStatusRequest request) {
        throw new UnsupportedOperationException("checkAuthStatus not supported");
    }

    /**
     * 完成认证流程，用中间态凭证换取最终的 credential 信息。
     */
    default AuthCompleteResponse completeAuth(AuthCompleteRequest request) {
        throw new UnsupportedOperationException("completeAuth not supported");
    }

    /**
     * 校验已有的 credential 是否仍然有效。
     *
     * @param credential credential 的 JSON 字符串
     */
    default boolean validateCredential(String credential) {
        throw new UnsupportedOperationException("validateCredential not supported");
    }

    /**
     * 刷新已有的 credential，返回新的 credential JSON 字符串。
     *
     * @param credential credential 的 JSON 字符串
     * @return 刷新后的 credential JSON 字符串
     */
    default String refreshCredential(String credential) {
        throw new UnsupportedOperationException("refreshCredential not supported");
    }
}
