package com.example.experience.domain.entity.entity;

import java.time.Instant;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.experience.domain.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 实体表
 * 这么命名是因为tm会有关键字冲突我也懒得改最简单的就是改表名
 */
@Entity
@Table(name = "entities", indexes = {
        @Index(name = "ix_entities_owner_platform_type", columnList = "user_id, platform, type")
})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class EntityRecord {

    @Id
    @Column(length = 32, nullable = false, updatable = false)
    private String id;

    @CreatedBy
    @Column(name = "created_by", length = 64, nullable = false)
    private String createdBy;

    /**
     * 数据归属压缩查询成本
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * 数据来源平台压缩查询成本
     */
    @Column(name = "platform", length = 64, nullable = true)
    private String platform;

    @Column(name = "type", length = 32, nullable = false)
    private String type;

    @Column(name = "display_name", length = 255, nullable = true)
    private String displayName;

    @Column(name = "relations", columnDefinition = "JSON", nullable = true)
    private String relations;

    @Column(name = "metadata", columnDefinition = "JSONB", nullable = true)
    private String metadata;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public static EntityRecord create(String id, User user, String platform, String type) {
        EntityRecord record = new EntityRecord();
        record.id = id;
        record.user = user;
        record.platform = platform;
        record.type = type;
        return record;
    }
}
