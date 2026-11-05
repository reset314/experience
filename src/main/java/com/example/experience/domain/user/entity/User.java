package com.example.experience.domain.user.entity;

import java.time.Instant;

import lombok.*;
import org.simpleframework.xml.Default;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id
    @Column(name = "id", length = 32, nullable = false, updatable = false)
    private String id;

    @Column(name = "username", length = 64, nullable = false, unique = true)
    private String username;

    @Column(name = "password_hash", length = 255, nullable = false)
    private String passwordHash;

    @Column(name = "email", length = 255, nullable = true, unique = true)
    private String email;

    @Column(name = "phone", length = 32, nullable = true, unique = true)
    private String phone;

    @Column(name = "status", length = 16, nullable = false)
    private String status = "active";

    @Column(name = "token_version", nullable = false)
    @Default
    private int tokenVersion = 0;

    @Column(name = "last_login_at")
    private Instant lastLoginAt;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public static User create(String id, String username, String email, String passwordHash) {
        User user = new User();
        user.id = id;
        user.username = username;
        user.email = email;
        user.passwordHash = passwordHash;
        return user;
    }
}