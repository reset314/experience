package com.example.experience.domain.media.entity;

import com.example.experience.domain.datasource.entity.UserDataSource;
import com.example.experience.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "data_source_id")
    private UserDataSource dataSource;

    //文件名
    @Column(name = "original_name")
    private String originalName;

    @Column(name = "mime_type", length = 128)
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

    @CreatedBy
    @Column(name = "created_by", length = 64, nullable = false)
    private String createdBy;
}
