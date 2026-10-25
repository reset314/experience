package com.example.experience.infrastructure.sync.adapter;

/**
 * 标记本次同步的来源
 */
public enum SyncTrigger {

    AUTO_SYNC("auto_sync"),

    MANUAL("manual");

    private final String sourceType;

    SyncTrigger(String sourceType) {
        this.sourceType = sourceType;
    }

    public String sourceType() {
        return sourceType;
    }
}
