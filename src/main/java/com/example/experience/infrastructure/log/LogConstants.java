package com.example.experience.infrastructure.log;

/**
 * 日志常量模块：统一管理所有日志相关的 Key 和事件类型
 */
public final class LogConstants {

    // ==================== MDC 上下文键（用于日志链路追踪） ====================
    public static final class MdcKeys {
        public static final String TRACE_ID = "traceId";
        public static final String USER_ID = "userId";
        public static final String CLIENT_IP = "clientIp";
        public static final String REQUEST_URI = "requestUri";
        public static final String EVENT_TYPE = "eventType";
        public static final String TENANT_ID = "tenantId";

        private MdcKeys() {}
    }

    public static final class EventTypes {
        // 认证
        public static final String USER_LOGIN = "user.login";
        public static final String USER_LOGOUT = "user.logout";
        // RBAC
        public static final String ROLE_CHANGE = "role.change";
        // 凭证
        public static final String CREDENTIAL_ACCESS = "credential.access";
        // 数据
        public static final String DATA_EXPORT = "data.export";
        public static final String DATA_DELETE = "data.delete";
        // 同步
        public static final String SYNC_RUN = "sync.run";
        public static final String SYNC_SUCCESS = "sync.success";
        public static final String SYNC_FAILED = "sync.failed";
        // 请求错误
        public static final String RESOURCE_NOT_FOUND = "resource.not_found";
        public static final String AUTH_FAILED = "auth.failed";
        public static final String ACCESS_DENIED = "access.denied";

        private EventTypes() {}
    }

    private LogConstants() {}
}