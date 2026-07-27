package com.example.experience.domain.user.entity;

import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "user_profiles")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@EntityListeners(AuditingEntityListener.class)
public class UserProfile {

    @Id
    @Column(name = "id", length = 32, nullable = false, updatable = false)
    private String id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "display_name", length = 128, nullable = true)
    private String displayName;

    @Column(name = "real_name", length = 64, nullable = true)
    private String realName;

    @Column(name = "id_card_hash", length = 255, nullable = true, unique = true)
    private String idCardHash;

    @Column(name = "id_card_encrypted", columnDefinition = "TEXT", nullable = true)
    private String idCardEncrypted;

    @Column(name = "id_verified_at", nullable = true)
    private Instant idVerifiedAt;

    @Column(name = "settings", columnDefinition = "JSONB", nullable = true)
    private String settings;

    @Column(name = "avatar_data", columnDefinition = "TEXT", nullable = true)
    private String avatarData;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}
