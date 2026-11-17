package com.example.experience.domain.event.entity;

import com.example.experience.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.sql.DataSource;
import java.time.Instant;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn( name = "data_source_id", nullable = false)
    private DataSource dataSource;

    @Column(name = "event_type")
    private String eventType;
    //TODO::必须是对应的adapter在manifest中声明过的,需要改动manifest侧

    @Column(name = "dedup_key")
    private String dedupKey;
    //幂等键
    //TODO::同data_source_id, event_type的dedup_key唯一

    //事件发生时间
    @Column(name = "occurred_at", nullable = false, updatable = false)
    private Instant occurredAt;

    @Column(name = "payload", columnDefinition = "JSONB", nullable = false)
    private String payload;

    @Column(name = "description", length = 512, nullable = true)
    private String description;

    @Column(name = "source_type", length = 16, nullable = false)
    @Builder.Default
    private SourceTypes sourceType = SourceTypes.AUTO_SYNC;

    @Column(name = "operator_kind", nullable = true)
    private ObjectKinds operator;

    @Column(name = "operator_id", nullable = true)
    private String operator_id;

    //对应的幂等键
    @Column(name = "operator_key", nullable = true)
    private String operator_key;

    @Column(name = "target_kind", nullable = true)
    private ObjectKinds target;

    @Column(name = "target_id", nullable = true)
    private String target_id;

    //对应的幂等键
    @Column(name = "target_key", nullable = true)
    private String target_key;

    @Column(name = "deleted_at", nullable = true)
    private Instant deletedAt;

    @Version
    @Column(name = "version", nullable = false)
    @Builder.Default
    private int version = 0;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @CreatedBy
    @Column(name = "created_by", length = 64, nullable = false)
    private String createdBy;

}


