package com.example.financial_control_app.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserModel, Long> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    Optional<UserModel> findByUsername(String username);
}
