package com.example.experience.domain.rbac;

import java.util.Arrays;

public enum Permission {
    USER_READ("user", "read"),
    USER_WRITE("user", "write"),
    USER_DELETE("user", "delete"),
    USER_MANAGE("user", "manage"),

    DATASOURCE_READ("datasource", "read"),
    DATASOURCE_WRITE("datasource", "write"),
    DATASOURCE_MANAGE("datasource", "manage"),

    EVENT_READ("event", "read"),
    EVENT_WRITE("event", "write"),

    SYNC_TRIGGER("sync", "trigger"),
    SYNC_MANAGE("sync", "manage"),

    PLATFORM_READ("platform", "read"),
    PLATFORM_WRITE("platform", "write"),
    PLATFORM_MANAGE("platform", "manage"),

    SETTING_MANAGE("setting", "manage");

    private final String resource;
    private final String action;

    Permission(String resource, String action) {
        this.resource = resource;
        this.action = action;
    }

    public String getResource() {
        return resource;
    }

    public String getAction() {
        return action;
    }

    public String getCode() {
        return resource + ":" + action;
    }

    public static Permission fromCode(String code) {
        return Arrays.stream(values())
            .filter(permission -> permission.getCode().equals(code))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Unknown permission code: " + code));
    }
}
