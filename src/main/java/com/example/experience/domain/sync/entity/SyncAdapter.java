package com.example.experience.domain.sync.entity;

import java.time.Instant;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "sync_adapters")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@EntityListeners(AuditingEntityListener.class)
public class SyncAdapter {

    @Id
    @Column(length = 32, nullable = false, updatable = false)
    private String id;

    @Column(name = "adapter_key", length = 128, nullable = false, unique = true)
    private String adapterKey;

    @Column(name = "class_name", length = 255, nullable = true)
    private String className;

    @Column(name = "display_name", length = 128, nullable = false)
    private String displayName;

    @Column(name = "description", columnDefinition = "TEXT", nullable = true)
    private String description;

    @CreatedBy
    @Column(name = "created_by", length = 64, nullable = false)
    private String createdBy;

    @Column(name = "visibility", length = 32, nullable = false)
    @Builder.Default
    private String visibility = "private";

    @Column(name = "status", length = 32, nullable = false)
    @Builder.Default
    private String status = "active";

    @Column(name = "version", length = 16, nullable = true)
    private String version;

    @Column(name = "metadata", columnDefinition = "JSONB", nullable = true)
    private String metadata;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}
