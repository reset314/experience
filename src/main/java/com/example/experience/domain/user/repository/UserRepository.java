package com.example.experience.domain.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.experience.domain.user.entity.User;

public interface UserRepository extends JpaRepository<User, String> {
    // Additional query methods can be defined here if needed
    Optional<User> findByUsername(String username);
}
