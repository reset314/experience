package com.example.experience.domain.sync.entity;

import com.example.experience.domain.datasource.entity.UserDataSource;
import com.example.experience.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Entity
@Table(name = "sync_logs")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class SyncLog {

    @Id
    @Column(length = 32, nullable = false, updatable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "data_source_id", nullable = false)
    private UserDataSource dataSource;

    @Column(name = "started_at", nullable = false)
    private Instant startedAt;

    @Column(name = "finished_at", nullable = true)
    private Instant finishedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 32, nullable = false)
    private SyncStatus status;

    @Column(name = "events_fetched", nullable = false)
    private Long eventsFetched = 0L;

    @Column(name = "events_inserted", nullable = false)
    private Long eventsInserted = 0L;

    @Column(name = "media_downloaded", nullable = false)
    private Long mediaDownloaded = 0L;

    @Column(name = "media_bytes", nullable = false)
    private Long mediaBytes = 0L;

    @Column(name = "error_message", columnDefinition = "TEXT", nullable = true)
    private String errorMessage;

    @Column(name = "context", columnDefinition = "JSONB", nullable = true)
    private String context;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @CreatedBy
    @Column(name = "created_by", length = 64, nullable = false)
    private String createdBy;

    public static SyncLog create(String id, UserDataSource dataSource, Instant startedAt, SyncStatus status) {
        SyncLog log = new SyncLog();
        log.id = id;
        log.dataSource = dataSource;
        log.startedAt = startedAt;
        log.status = status;
        return log;
    }
}
