package com.etsia.user.infrastructure.repository;

import com.etsia.common.domain.model.sub.Email;

import com.etsia.common.infrastructure.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserRepository extends JpaRepository<User, Integer> {
    User findByEmail(Email email);
    boolean existsByEmail(Email email);
}
