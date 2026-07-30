package com.example.experience.application.event.service;

import java.util.List;

import com.example.experience.application.event.dto.CreateEventRequest;
import com.example.experience.application.event.dto.EventResponse;

public interface EventService {

    EventResponse createEvent(String createdBy, CreateEventRequest request);

    List<EventResponse> listEvents(String createdBy);

    EventResponse getEvent(String createdBy, String eventId);
}
