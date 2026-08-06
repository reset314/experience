package com.example.experience.infrastructure.sync.adapter;

import java.util.Collections;
import java.util.List;

import com.example.experience.domain.event.entity.Event;

public record SyncResult(
        List<Event> events,
        Long eventsFetched,
        Long eventsInserted,
        Long mediaDownloaded,
        Long mediaBytes,
        String message) {

    public SyncResult {
        events = events != null ? List.copyOf(events) : Collections.emptyList();
    }

    public static SyncResult empty() {
        return new SyncResult(Collections.emptyList(), 0L, 0L, 0L, 0L, null);
    }
}
