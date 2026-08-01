package com.example.experience.application.sync.dto;

public record SyncLogResponse(
    String id,
    String createdBy,
    String dataSourceId,
    String dataSourceName,
    String startedAt,
    String finishedAt,
    String status,
    Long eventsFetched,
    Long eventsInserted,
    Long mediaDownloaded
) {

}
