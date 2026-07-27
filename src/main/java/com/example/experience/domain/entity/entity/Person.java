package com.example.experience.domain.entity.entity;

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
@Table(name = "persons")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Person {

    @Id
    @Column(length = 32, nullable = false, updatable = false)
    private String id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "canonical_entity_id", length = 32, nullable = true)
    private String canonicalEntityId;

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

    public static Person create(String id, User user) {
        Person person = new Person();
        person.id = id;
        person.user = user;
        return person;
    }
}
