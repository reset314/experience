package com.example.experience.application.event.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.experience.application.event.dto.CreateEventRequest;
import com.example.experience.application.event.dto.EventResponse;
import com.example.experience.application.event.service.EventService;
import com.example.experience.common.utils.Uuid7Utils;
import com.example.experience.domain.entity.entity.EntityRecord;
import com.example.experience.domain.entity.repository.EntityRecordRepository;
import com.example.experience.domain.event.entity.Event;
import com.example.experience.domain.event.repository.EventRepository;
import com.example.experience.domain.user.entity.User;
import com.example.experience.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final EntityRecordRepository entityRecordRepository;

    @Override
    @Transactional
    public EventResponse createEvent(String createdBy, CreateEventRequest request) {
        User user = userRepository.findById(createdBy)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Event.EventBuilder builder = Event.builder()
            .id(Uuid7Utils.generateUuid7())
            .user(user)
            .deviceMac(request.deviceMac())
            .platform(request.platform())
            .eventType(request.eventType())
            .occurredAt(request.occurredAt())
            .payload(request.payload());

        if (request.description() != null) {
            builder.description(request.description());
        }
        if (request.sourceType() != null) {
            builder.sourceType(request.sourceType());
        }
        if (request.operatorId() != null) {
            builder.operator(findEntityRecord(request.operatorId()));
        }
        if (request.targetId() != null) {
            builder.target(findEntityRecord(request.targetId()));
        }

        Event event = eventRepository.save(builder.build());
        return toResponse(event);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventResponse> listEvents(String createdBy) {
        return eventRepository.findByCreatedBy(createdBy).stream()
            .map(this::toResponse)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public EventResponse getEvent(String createdBy, String eventId) {
        Event event = eventRepository.findByIdAndCreatedBy(eventId, createdBy)
            .orElseThrow(() -> new IllegalArgumentException("Event not found"));
        return toResponse(event);
    }

    private EntityRecord findEntityRecord(String id) {
        return entityRecordRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Entity record not found: " + id));
    }

    private EventResponse toResponse(Event event) {
        return new EventResponse(
            event.getId(),
            event.getCreatedBy(),
            event.getUser() != null ? event.getUser().getId() : null,
            event.getOperator() != null ? event.getOperator().getId() : null,
            event.getTarget() != null ? event.getTarget().getId() : null,
            event.getDeviceMac(),
            event.getPlatform(),
            event.getEventType(),
            event.getOccurredAt(),
            event.getSourceType(),
            event.getDescription(),
            event.getCreatedAt()
        );
    }
}
