package com.example.experience.domain.datasource.entity;

import com.example.experience.domain.platform.entity.Platform;
import com.example.experience.domain.sync.entity.SyncAdapter;
import com.example.experience.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

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

    @CreatedBy
    @Column(name = "created_by", length = 64, nullable = false)
    private String createdBy;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "platform_id", nullable = false)
    private Platform platform;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "adapter_id", nullable = false)
    private SyncAdapter adapter;

    @Column(name = "source_identifier", length = 255)
    private String sourceIdentifier;

    @Column(name = "self_entity_id", length = 32)
    private String selfEntityId;

    @Column(name = "display_name", length = 128, nullable = true)
    private String displayName;

    @Enumerated(EnumType.STRING)
    @Column(name = "auth_status", length = 32, nullable = false)
    @Builder.Default
    private AuthStatus authStatus = AuthStatus.PENDING;

    //最近的message(给用户)
    @Column(name = "interaction_hint", columnDefinition = "TEXT", nullable = true)
    private String interactionHint;

    @Column(name = "sync_status", length = 32, nullable = false)
    @Builder.Default
    private SyncStatus syncStatus = SyncStatus.IDLE;

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

    //认证成功time
    @Column(name = "activated_at")
    private Instant activatedAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @CreatedDate
    @Column(name = "create_at", nullable = false)
    private Instant createdAt;
}
