package com.etsia.interaction.domain.service.conversation;

import com.etsia.interaction.domain.repository.conversation.ConversationUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FollowConversationUserDomainService {

    private final ConversationUserRepository conversationUserRepository;

    public boolean isFollowing(Integer FollowerId, Integer ConversationId) {
        return conversationUserRepository.isFollowing(FollowerId, ConversationId);
    }
}
