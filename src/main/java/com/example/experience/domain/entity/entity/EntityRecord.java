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

import javax.sql.DataSource;
import java.time.Instant;

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

    /**
     * 数据归属压缩查询成本
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "data_source_id", nullable = false)
    private DataSource dataSource;

    @Column(name = "type", length = 32, nullable = false)
    private String type;

    //幂等键
    @Column(name = "identity_key")
    private String identityKey;

    @Column(name = "display_name", length = 255, nullable = true)
    private String displayName;

    @Column(name = "metadata", columnDefinition = "JSONB", nullable = true)
    private String metadata;

    @Version
    @Column(name = "version")
    private Integer version;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @CreatedBy
    @Column(name = "created_by", length = 64, nullable = false)
    private String createdBy;

    public static EntityRecord create(String id, User user, DataSource dataSource, String type) {
        EntityRecord record = new EntityRecord();
        record.id = id;
        record.user = user;
        record.dataSource = dataSource;
        record.type = type;
        return record;
    }

    public void updateVersion(){
        this.version+=1;
    }
}
