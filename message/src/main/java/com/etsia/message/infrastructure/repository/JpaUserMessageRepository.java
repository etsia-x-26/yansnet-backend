package com.etsia.message.infrastructure.repository;

import com.etsia.common.infrastructure.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("messageUserRepository")
public interface JpaUserMessageRepository extends JpaRepository<User, Integer> {
}
