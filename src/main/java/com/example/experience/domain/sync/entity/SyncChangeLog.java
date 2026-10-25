package com.example.experience.domain.sync.entity;

import java.time.Instant;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sync_change_logs", indexes = {
    @Index(name = "ix_scl_target", columnList = "target_kind, target_id, id"),
    @Index(name = "ix_scl_user_time", columnList = "user_id, created_at"),
    @Index(name = "ix_scl_sync_log", columnList = "sync_log_id")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@EntityListeners(AuditingEntityListener.class)
public class SyncChangeLog {

    @Id
    @Column(length = 32, nullable = false, updatable = false)
    private String id;

    @Column(name = "user_id", length = 32, nullable = false, updatable = false)
    private String userId;

    @Column(name = "target_kind", length = 16, nullable = false, updatable = false)
    private String targetKind;

    /**
     * 目标对象的id
     */
    @Column(name = "target_id", length = 32, nullable = false, updatable = false)
    private String targetId;

    /**
     * 解释当前日志所属的对象类型防止删除后不止到类型
     */
    @Column(name = "target_type", length = 64, nullable = false, updatable = false)
    private String targetType;

    /**
     * 操作类型create update delete
     */
    @Column(name = "op", length = 16, nullable = false, updatable = false)
    private String op;

    @Column(name = "diff", columnDefinition = "JSONB", nullable = false, updatable = false)
    private String diff;

    @Column(name = "platform_name", length = 64, nullable = false, updatable = false)
    private String platformName;

    @Column(name = "data_source_id", length = 32, nullable = true, updatable = false)
    private String dataSourceId;

    @Column(name = "sync_log_id", length = 32, nullable = true, updatable = false)
    private String syncLogId;

    @Column(name = "occurred_at", nullable = true, updatable = false)
    private Instant occurredAt;

    @CreatedBy
    @Column(name = "created_by", length = 64, nullable = true, updatable = false)
    private String createdBy;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
}
