package com.example.experience.application.event.dto;

public record DeleteEventRequest(
    String UserId,
    String eventId
) {
}
