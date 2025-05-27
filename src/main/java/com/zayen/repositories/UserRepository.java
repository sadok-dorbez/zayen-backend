package com.zayen.repositories;

import com.zayen.entities.User;
import com.zayen.enumeration.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
    long countByRole(Role role);
}