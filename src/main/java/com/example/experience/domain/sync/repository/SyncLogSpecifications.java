package com.example.experience.domain.sync.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.data.jpa.domain.Specification;

import com.example.experience.application.sync.dto.SyncLogListRequest;
import com.example.experience.domain.sync.entity.SyncLog;

import jakarta.persistence.criteria.Predicate;

public class SyncLogSpecifications {

    private SyncLogSpecifications() {
    }

    public static Specification<SyncLog> fromRequest(SyncLogListRequest request) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.equal(root.get("createdBy"), request.userId()));

            if (request.status() != null && !request.status().isBlank()) {
                predicates.add(cb.equal(root.get("status"), request.status()));
            }

            if (request.dataSourceId() != null && !request.dataSourceId().isBlank()) {
                predicates.add(cb.equal(root.get("dataSource").get("id"), request.dataSourceId()));
            }

            if (request.q() != null && !request.q().isBlank()) {
                String pattern = "%" + request.q().trim().toLowerCase(Locale.ROOT) + "%";
                Predicate displayNameLike = cb.like(cb.lower(root.get("dataSource").get("displayName")), pattern);
                Predicate statusLike = cb.like(cb.lower(root.get("status")), pattern);
                predicates.add(cb.or(displayNameLike, statusLike));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
