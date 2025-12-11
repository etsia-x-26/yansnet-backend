package com.etsia.interaction.application.service.conversation;

import com.etsia.common.domain.model.sub.ConversationRole;
import com.etsia.interaction.domain.repository.conversation.ConversationUserRepository;
import com.etsia.interaction.domain.service.conversation.FollowConversationUserDomainService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;


@Service
@AllArgsConstructor
public class FollowConversationUseCase {

    private final ConversationUserRepository conversationUserRepository;
    private final FollowConversationUserDomainService followConversationUserDomainService;

    public void execute(Map<Integer, ConversationRole> followerRoles, Integer conversationId) {
        if (followerRoles == null || followerRoles.isEmpty()) {
            return;
        }

        for (Map.Entry<Integer, ConversationRole> entry : followerRoles.entrySet()) {
            Integer followerId = entry.getKey();
            ConversationRole role = entry.getValue();

            if (followConversationUserDomainService.isFollowing(followerId, conversationId)) {
                throw new IllegalArgumentException("Follow already exists for user " + followerId);
            }

            // Ajouter le follower avec son rôle spécifique
            conversationUserRepository.Follow(
                    Arrays.asList(followerId),
                    conversationId,
                    role
            );
        }
    }
}
