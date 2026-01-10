package com.etsia.interaction.infrastructure.repository.conversation;

import com.etsia.common.infrastructure.entities.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("conversation_rep")
public interface JpaConversationRepository extends JpaRepository<Conversation, Integer> {

}
