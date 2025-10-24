package com.etsia.interaction.infrastructure.repository.conversation;

import com.etsia.common.infrastructure.entities.ConversationUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaConversationUserRepository extends JpaRepository<ConversationUser, Integer> {

}
