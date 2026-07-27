package com.example.experience.domain.entity.entity;

import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "entities")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class EntityRecord {

    @Id
    @Column(length = 32, nullable = false, updatable = false)
    private String id;

    @Column(name = "type", length = 32, nullable = false)
    private String type;

    @Column(name = "display_name", length = 255, nullable = true)
    private String displayName;

    @Column(name = "canonical_entity_id", length = 32, nullable = true)
    private String canonicalEntityId;

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

    public static EntityRecord create(String id, String type) {
        EntityRecord record = new EntityRecord();
        record.id = id;
        record.type = type;
        return record;
    }
}
