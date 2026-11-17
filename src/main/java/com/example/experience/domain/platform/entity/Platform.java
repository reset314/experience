package com.example.experience.domain.platform.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

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

    @Column(name = "name", length = 64, nullable = false, unique = true)
    private String name;

    @Column(name = "display_name", length = 128, nullable = false)
    private String displayName;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @CreatedBy
    @Column(name = "created_by", length = 64, nullable = false)
    private String createdBy;

    @Column(name = "visibility", length = 32, nullable = false)
    @Builder.Default
    private String visibility = "private";

    //uri
    @Column(name = "icon", length = 255, nullable = true) 
    private String icon;

    @Column(name = "metadata", columnDefinition = "JSONB", nullable = true)
    private String metadata;

    @Column(name = "config", columnDefinition = "JSON", nullable = true)
    private String config;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Column(name = "deleted_at", nullable = true)
    private Instant deletedAt;
}
