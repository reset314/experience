package com.example.experience.application.sync.dto;

public record SyncLogDetailResponse(
    String id,
    String createdBy,
    String dataSourceId,
    String dataSourceName,
    String startedAt,
    String finishedAt,
    String status,
    String errorMessage,
    String context,
    Long eventsFetched,
    Long eventsInserted,
    Long mediaDownloaded
) {

}
