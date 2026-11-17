package com.example.experience.domain.entity.entity;

import com.example.experience.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;


/**
 * 在创建关系的时候应该注意是否为双向
 * */
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

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column (name = "form_id", length = 32, nullable = false)
    private String formId;

    @Column (name = "to_id", length = 32, nullable = false)
    private String toId;

    @Enumerated(EnumType.STRING)
    @Column (name = "from_type", length = 32, nullable = false)
    private PEType fromType;

    @Enumerated(EnumType.STRING)
    @Column (name = "to_type", length = 32, nullable = false)
    private PEType toType;

    //change:由规定type改为自由string
    @Column (name = "relation_type", length = 256, nullable = false)
    private String relationType;

    @Column (name = "description", columnDefinition = "TEXT", nullable = true)
    private String description;

    //关系建立time
    @Column( name = "established_at")
    private Instant establishedAt;

    @Column( name = "metadata", columnDefinition = "JSONB")
    private String metadata;

    @CreatedDate
    @Column (name = "created_at")
    private Instant createdAt;

    @UpdateTimestamp
    @Column (name = "updated_at")
    private Instant updatedAt;

    //TODO::提供create函数及其变种要求可以快捷创建双向以及单向关系
} 