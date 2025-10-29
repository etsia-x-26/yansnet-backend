package com.etsia.interaction.infrastructure.repository.conversation;

import com.etsia.common.infrastructure.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaInteractionUserRepository extends JpaRepository<User, Integer> {
}
