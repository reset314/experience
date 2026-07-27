package com.example.experience.domain.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.experience.domain.event.entity.Event;

public interface EventRepository extends JpaRepository<Event, String> {
}
