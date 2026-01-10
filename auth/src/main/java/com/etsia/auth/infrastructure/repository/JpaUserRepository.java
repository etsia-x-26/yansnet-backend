package com.etsia.auth.infrastructure.repository;

import com.etsia.common.domain.model.sub.Email;
import com.etsia.common.infrastructure.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaUserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(Email email);
    boolean existsByEmail(Email email);
}