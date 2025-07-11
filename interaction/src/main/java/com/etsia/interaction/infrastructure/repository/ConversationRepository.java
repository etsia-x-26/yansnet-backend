package com.etsia.interaction.infrastructure.repository;

import com.etsia.common.infrastructure.entities.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversationRepository extends JpaRepository<Conversation, Integer> {
}