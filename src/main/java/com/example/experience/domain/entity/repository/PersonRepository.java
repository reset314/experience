package com.example.experience.domain.entity.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.example.experience.domain.entity.entity.Person;

public interface PersonRepository extends JpaRepository<Person, String>, JpaSpecificationExecutor<Person> {
    List<Person> findByUserId(String userId);

    List<Person> findByCreatedBy(String createdBy);

    Optional<Person> findByIdAndCreatedBy(String id, String createdBy);
}
