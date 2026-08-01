package com.example.experience.application.sync.dto;

public record SyncLogListRequest(
    String userId,
    String status,
    String dataSourceId,
    int page,
    int size
) {

}
