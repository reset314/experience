package com.example.experience.application.sync.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record SyncLogListRequest(
    String userId,
    String status,
    String dataSourceId,
    String q,

    @Min(0) Integer page,
    @Min(1) @Max(100) Integer size,
    String sortBy,
    String sortDirection
) {

}
