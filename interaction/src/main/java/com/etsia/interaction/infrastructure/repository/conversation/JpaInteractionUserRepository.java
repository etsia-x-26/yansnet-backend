package com.etsia.interaction.infrastructure.repository.conversation;

import com.etsia.common.infrastructure.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaInteractionUserRepository extends JpaRepository<User, Integer> {
}
