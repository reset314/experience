package com.example.experience.domain.media.entity;

import java.time.Instant;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.experience.domain.datasource.entity.UserDataSource;
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
@Table(name = "media_files")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@EntityListeners(AuditingEntityListener.class)
public class MediaFile {

    @Id
    @Column(length = 32, nullable = false, updatable = false)
    private String id;

    @CreatedBy
    @Column(name = "created_by", length = 64, nullable = false)
    private String createdBy;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "data_source_id", nullable = true)
    private UserDataSource dataSource;

    @Column(name = "original_name", length = 255, nullable = true)
    private String originalName;

    @Column(name = "mime_type", length = 128, nullable = true)
    private String mimeType;

    @Column(name = "size_bytes", nullable = false)
    @Builder.Default
    private Long sizeBytes = 0L;

    @Column(name = "storage_path", length = 512, nullable = false, unique = true)
    private String storagePath;

    @Column(name = "storage_bucket", length = 64, nullable = false)
    @Builder.Default
    private String storageBucket = "experience";

    @Column(name = "checksum_sha256", length = 64, nullable = true)
    private String checksumSha256;

    @Column(name = "metadata", columnDefinition = "JSONB", nullable = true)
    private String metadata;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}
