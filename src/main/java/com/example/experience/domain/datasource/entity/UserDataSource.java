package com.example.experience.domain.datasource.entity;

import java.time.Instant;

import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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
@Table(name = "user_data_sources")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@EntityListeners(AuditingEntityListener.class)
public class UserDataSource {

    @Id
    @Column(length = 32, nullable = false, updatable = false)
    private String id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "platform_name", length = 64, nullable = false)
    private String platformName;

    @Column(name = "source_identifier", length = 255, nullable = false)
    private String sourceIdentifier;

    @Column(name = "display_name", length = 128, nullable = true)
    private String displayName;

    @Column(name = "sync_status", length = 32, nullable = false)
    @Builder.Default
    private String syncStatus = "idle";

    @Column(name = "auth_status", length = 32, nullable = false)
    @Builder.Default
    private String authStatus = "authorized";

    @Column(name = "requires_user_interaction", nullable = false)
    @Builder.Default
    private Boolean requiresUserInteraction = false;

    @Column(name = "interaction_hint", columnDefinition = "TEXT", nullable = true)
    private String interactionHint;

    @Column(name = "last_sync_at", nullable = true)
    private Instant lastSyncAt;

    @Column(name = "last_sync_error", columnDefinition = "TEXT", nullable = true)
    private String lastSyncError;

    @Column(name = "total_events_count", nullable = false)
    @Builder.Default
    private Long totalEventsCount = 0L;

    @Column(name = "total_storage_bytes", nullable = false)
    @Builder.Default
    private Long totalStorageBytes = 0L;

    @Column(name = "sync_config", columnDefinition = "JSONB", nullable = true)
    private String syncConfig;

    @Column(name = "activated_at", nullable = false)
    @Builder.Default
    private Instant activatedAt = Instant.now();

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Column(name = "deleted_at", nullable = true)
    private Instant deletedAt;
}
