package com.example.experience.domain.datasource.entity;

import com.example.experience.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;


/**
 * !!!不加密
 * */
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

    @ManyToOne(fetch= FetchType.LAZY,optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch= FetchType.LAZY,optional = false)
    @JoinColumn(name = "data_source_id", nullable = false)
    private UserDataSource dataSource;

    @Column(name = "credential_type", length = 32, nullable = false)
    private String credentialType;

    @Column(name = "credential", columnDefinition = "JSONB", nullable = false)
    private String credential;

    @Column(name = "status", length = 32, nullable = false)
    private CredentialStatus status;

    @Column(name = "expires_at", nullable = true)
    private Instant expiresAt;

    @Column(name = "last_used_at", nullable = true)
    private Instant lastUsedAt;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @CreatedBy
    @Column(name = "created_by", length = 64, nullable = false)
    private String createdBy;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public static UserCredential create(String id, User user, UserDataSource dataSource,
                                        String credentialType, String credential) {
        UserCredential userCredential = new UserCredential();
        userCredential.id = id;
        userCredential.user = user;
        userCredential.dataSource = dataSource;
        userCredential.credentialType = credentialType;
        userCredential.credential = credential;
        return userCredential;
    }
}
