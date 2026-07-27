package com.example.experience.domain.rbac.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "permissions", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"resource", "action"})
})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Permission {

    @Id
    @Column(length = 32, nullable = false, updatable = false)
    private String id;

    @Column(length = 64, nullable = false)
    private String resource;

    @Column(length = 64, nullable = false)
    private String action;

    @Column(columnDefinition = "TEXT", nullable = true)
    private String description;

    public static Permission create(String id, String resource, String action, String description) {
        Permission permission = new Permission();
        permission.id = id;
        permission.resource = resource;
        permission.action = action;
        permission.description = description;
        return permission;
    }
}