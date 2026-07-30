package com.example.experience.application.event.dto;

public record GetEventListRequest(
    String userId,
    long total,
    int page,
    int size
) {
}
