package com.example.experience.domain.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.experience.domain.entity.entity.Person;

public interface PersonRepository extends JpaRepository<Person, String> {
    List<Person> findByUserId(String userId);
}
