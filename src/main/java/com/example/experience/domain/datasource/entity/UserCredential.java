package com.example.experience.domain.datasource.entity;

import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.experience.domain.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_credentials")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class UserCredential {

    @Id
    @Column(length = 32, nullable = false, updatable = false)
    private String id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "data_source_id", nullable = false)
    private UserDataSource dataSource;

    @Column(name = "credential_type", length = 32, nullable = false)
    private String credentialType;

    @Column(name = "encrypted_credential", columnDefinition = "TEXT", nullable = false)
    private String encryptedCredential;

    @Column(name = "encrypted_extra", columnDefinition = "TEXT", nullable = true)
    private String encryptedExtra;

    @Column(name = "status", length = 32, nullable = false)
    private String status = "active";

    @Column(name = "expires_at", nullable = true)
    private Instant expiresAt;

    @Column(name = "last_used_at", nullable = true)
    private Instant lastUsedAt;

    @Column(name = "auth_context", columnDefinition = "JSONB", nullable = true)
    private String authContext;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public static UserCredential create(String id, User user, UserDataSource dataSource,
                                        String credentialType, String encryptedCredential) {
        UserCredential credential = new UserCredential();
        credential.id = id;
        credential.user = user;
        credential.dataSource = dataSource;
        credential.credentialType = credentialType;
        credential.encryptedCredential = encryptedCredential;
        return credential;
    }
}
