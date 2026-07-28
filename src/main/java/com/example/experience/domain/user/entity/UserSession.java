package com.example.experience.domain.user.entity;

import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_sessions")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class UserSession {

    @Id
    @Column(name = "id", length = 32, nullable = false, updatable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

    @Column(name = "refresh_token_hash", length = 64, nullable = false, unique = true)
    private String refreshTokenHash;

    @Column(name = "device_name", length = 128)
    private String deviceName;

    @Column(name = "ip_address", length = 64)
    private String ipAddress;

    @Column(name = "user_agent", columnDefinition = "TEXT")
    private String userAgent;

    @Column(name = "status", length = 16, nullable = false)
    private String status = "active";

    @Column(name = "issued_at", nullable = false, updatable = false)
    private Instant issuedAt;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column(name = "last_used_at")
    private Instant lastUsedAt;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Transient
    private String plainRefreshToken;

    public static UserSession create(String id, User user, String refreshTokenHash,
                                     Instant issuedAt, Instant expiresAt,
                                     String deviceName, String ipAddress, String userAgent) {
        UserSession session = new UserSession();
        session.id = id;
        session.user = user;
        session.refreshTokenHash = refreshTokenHash;
        session.issuedAt = issuedAt;
        session.expiresAt = expiresAt;
        session.deviceName = deviceName;
        session.ipAddress = ipAddress;
        session.userAgent = userAgent;
        return session;
    }

    public void revoke() {
        this.status = "revoked";
    }

    public void markUsed() {
        this.lastUsedAt = Instant.now();
    }

    public boolean isExpired() {
        return Instant.now().isAfter(this.expiresAt);
    }

    public boolean isActive() {
        return "active".equals(this.status) && !isExpired();
    }
}
