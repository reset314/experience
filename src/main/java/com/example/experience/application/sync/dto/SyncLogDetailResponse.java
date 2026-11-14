package com.example.experience.application.sync.dto;

import com.example.experience.domain.sync.entity.SyncStatus;

public record SyncLogDetailResponse(
    String id,
    String createdBy,
    String dataSourceId,
    String dataSourceName,
    String startedAt,
    String finishedAt,
    SyncStatus status,
    String errorMessage,
    String context,
    Long eventsFetched,
    Long eventsInserted,
    Long mediaDownloaded
) {

}
