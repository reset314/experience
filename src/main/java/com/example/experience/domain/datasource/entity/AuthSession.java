package com.example.experience.domain.datasource.entity;

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

/**
 * 认证会话实体，用于持久化多步骤登录流程中的中间状态。
 * 例如二维码登录中的 qrcodeKey、OAuth 中的 state 等。
 */
@Entity
@Table(name = "auth_sessions")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@EntityListeners(AuditingEntityListener.class)
public class AuthSession {

    @Id
    @Column(length = 32, nullable = false, updatable = false)
    private String id;

    @Column(name = "platform", length = 64, nullable = false)
    private String platform;

    @Column(name = "session_type", length = 32, nullable = false)
    private String sessionType;

    @Column(name = "external_key", length = 255, nullable = false)
    private String externalKey;

    @Column(name = "status", length = 32, nullable = false)
    @Builder.Default
    private String status = "pending";

    @Column(name = "expires_at", nullable = true)
    private Instant expiresAt;

    @Column(name = "credential_id", length = 32, nullable = true)
    private String credentialId;

    @CreatedBy
    @Column(name = "created_by", length = 64, nullable = false)
    private String createdBy;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public static AuthSession create(String id, String platform, String sessionType,
                                     String externalKey, Instant expiresAt) {
        AuthSession session = new AuthSession();
        session.id = id;
        session.platform = platform;
        session.sessionType = sessionType;
        session.externalKey = externalKey;
        session.expiresAt = expiresAt;
        return session;
    }

    public void complete(String credentialId) {
        this.status = "completed";
        this.credentialId = credentialId;
    }

    public void expire() {
        this.status = "expired";
    }

    public boolean isExpired() {
        return "expired".equals(status)
                || (expiresAt != null && Instant.now().isAfter(expiresAt));
    }
}
