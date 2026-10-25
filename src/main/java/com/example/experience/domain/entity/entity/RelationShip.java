package com.example.experience.domain.entity.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity 
@Table (name = "relationships")
@Getter 
@Setter 
@AllArgsConstructor 
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RelationShip {

    @Id
    @Column(length = 32, nullable = false, updatable = false)
    private String id;

    @Column (name = "form_id", length = 32, nullable = false)
    private String formId;

    @Column (name = "to_id", length = 32, nullable = false)
    private String toId;

    @Column (name = "from_type", length = 32, nullable = false)
    private String fromType;

    @Column (name = "to_type", length = 32, nullable = false)
    private String toType;

    @Column (name = "relation_type", length = 256, nullable = false)
    private RelationType relationType;

    @Column (name = "description", columnDefinition = "TEXT", nullable = true)
    private String description;

} 