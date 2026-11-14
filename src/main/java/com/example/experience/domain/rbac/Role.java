package com.example.experience.domain.rbac;

import java.util.Arrays;

public enum Role {
    SUPERADMIN("superadmin", "超级管理员"),
    ADMIN("admin", "管理员"),
    USER("user", "普通用户"),
    AUDITOR("auditor", "审计员");

    private final String code;
    private final String displayName;

    Role(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    public String getCode() {
        return code;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static Role fromCode(String code) {
        return Arrays.stream(values())
            .filter(role -> role.code.equals(code))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Unknown role code: " + code));
    }
}
