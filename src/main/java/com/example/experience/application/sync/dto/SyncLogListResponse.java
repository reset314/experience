package com.example.experience.application.sync.dto;

import java.util.List;

public record SyncLogListResponse(
    List<SyncLogResponse> syncLogs,
    long total,
    int page,
    int size
) {

}
