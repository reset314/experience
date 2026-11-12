package com.example.experience.domain.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.simpleframework.xml.Default;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id
    @Column(name = "id", length = 32, nullable = false, updatable = false)
    private String id;

    @Column(name = "username", length = 64, nullable = false, unique = true, updatable = false)
    private String username;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "phone", length = 32, unique = true)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 16, nullable = false)
    private UserStatus status = UserStatus.ACTIVE;

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

    private void bumpTokenVersion(){
        this.tokenVersion += 1;
    }

    public void suspend(){
        this.status = UserStatus.SUSPENDED;
        bumpTokenVersion();
    }

    public void activate() {
        this.status = UserStatus.ACTIVE;
    }

    public void recordLogin(Instant loginAt) {
        this.lastLoginAt = loginAt;
    }

    public void changePassword(String newPasswordHash) {
        this.passwordHash = newPasswordHash;
        bumpTokenVersion();
    }

    public void updateLastLoginAt(Instant lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    public void changePhone(String phone) {
        this.phone = phone;
    }

    public void changeEmail(String email) {
        this.email = email;
    }


}