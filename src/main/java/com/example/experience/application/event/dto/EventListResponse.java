package com.example.experience.application.event.dto;

import java.util.List;

public record EventListResponse(
    List<EventDetailResponse> events,
    int page,
    int size
) {
}
