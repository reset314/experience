package com.example.experience.application.event.dto;

import java.util.List;

public record GetEventListRequest(
    List<EventDetailResponse> events,
    long total,
    int page,
    int size
) {
}
