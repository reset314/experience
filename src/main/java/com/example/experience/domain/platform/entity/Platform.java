package com.example.experience.domain.platform.entity;

import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.experience.domain.sync.entity.SyncAdapter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "platforms", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"name", "created_by"}),
    @UniqueConstraint(columnNames = {"created_by", "adapter_id"})
})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Platform {

    @Id
    @Column(length = 32, nullable = false, updatable = false)
    private String id;

    @Column(name = "name", length = 64, nullable = false)
    private String name;

    @Column(name = "display_name", length = 128, nullable = false)
    private String displayName;

    @Column(name = "description", columnDefinition = "TEXT", nullable = true)
    private String description;

    @ManyToOne
    @JoinColumn(name = "adapter_id", nullable = true)
    private SyncAdapter adapter;

    @Column(name = "created_by", length = 64, nullable = false)
    private String createdBy;

    @Column(name = "visibility", length = 32, nullable = false)
    @Builder.Default
    private String visibility = "private";

    @Column(name = "icon", length = 255, nullable = true)
    private String icon;

    @Column(name = "metadata", columnDefinition = "JSONB", nullable = true)
    private String metadata;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Column(name = "deleted_at", nullable = true)
    private Instant deletedAt;
}
