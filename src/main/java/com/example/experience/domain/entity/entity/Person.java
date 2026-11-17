package com.example.experience.domain.entity.entity;

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
 * 个人信息表,
 * 目前设计为只能为用户添加,对等于通讯录
 */
@Entity
@Table(name = "persons")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Person {

    @Id
    @Column(length = 32, nullable = false, updatable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "is_self")
    private boolean isSelf;//TODO::需要处理每个用户恰好一条的需求

    @Column(name = "display_name", length = 255, nullable = true)
    private String displayName;

    @Column(name = "real_name", length = 128)
    private String realName;

    @Column(name = "relations", columnDefinition = "JSON", nullable = true)
    private String relations;

    @Column(name = "metadata", columnDefinition = "JSONB", nullable = true)
    private String metadata;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @CreatedBy
    @Column(name = "created_by", length = 64, nullable = false)
    private String createdBy;

    public static Person create(String id, User user) {
        Person person = new Person();
        person.id = id;
        person.user = user;
        return person;
    }
}
