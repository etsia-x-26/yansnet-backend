package com.etsia.post.infrastructure.repository;

import com.etsia.common.infrastructure.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserPostUserRepository  extends JpaRepository<User, Integer> {
}
