package com.example.experience.domain.event.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.example.experience.domain.event.entity.Event;

public interface EventRepository extends JpaRepository<Event, String>, JpaSpecificationExecutor<Event> {
    List<Event> findByCreatedBy(String createdBy);

    Optional<Event> findByIdAndCreatedBy(String id, String createdBy);
}
