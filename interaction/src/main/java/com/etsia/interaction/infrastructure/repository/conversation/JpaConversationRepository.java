package com.etsia.interaction.infrastructure.repository.conversation;

import com.etsia.common.infrastructure.entities.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaConversationRepository extends JpaRepository<Conversation, Integer> {

}
