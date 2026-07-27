package com.example.experience.domain.event.entity;

import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.experience.domain.entity.entity.EntityRecord;
import com.example.experience.domain.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "events")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Event {

    @Id
    @Column(length = 32, nullable = false, updatable = false)
    private String id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "operator_id", nullable = true)
    private EntityRecord operator;

    @ManyToOne
    @JoinColumn(name = "target_id", nullable = true)
    private EntityRecord target;

    @Column(name = "device_mac", length = 32, nullable = false)
    private String deviceMac;

    @Column(name = "platform", length = 64, nullable = false)
    private String platform;

    @Column(name = "event_type", length = 64, nullable = false)
    private String eventType;

    @Column(name = "occurred_at", nullable = false)
    private Instant occurredAt;

    @Column(name = "payload", columnDefinition = "JSONB", nullable = false)
    private String payload;

    @Column(name = "source_type", length = 16, nullable = false)
    @Builder.Default
    private String sourceType = "auto_sync";

    @Column(name = "deleted_at", nullable = true)
    private Instant deletedAt;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
}
