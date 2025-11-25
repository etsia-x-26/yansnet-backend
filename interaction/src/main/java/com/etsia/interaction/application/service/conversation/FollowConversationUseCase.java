package com.etsia.interaction.application.service.conversation;

import com.etsia.common.domain.model.sub.ConversationRole;
import com.etsia.common.domain.model.sub.ConversationRoleRenew;
import com.etsia.interaction.domain.repository.conversation.ConversationUserRepository;
import com.etsia.interaction.domain.service.conversation.FollowConversationUserDomainService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;


@Service
@AllArgsConstructor
public class FollowConversationUseCase {

    private final ConversationUserRepository conversationUserRepository;
    private final FollowConversationUserDomainService followConversationUserDomainService;

    public void execute(Integer[] followerIds, Integer conversationId, ConversationRole role) {
        for (int i = 0; i < followerIds.length; i++) {
            if (followConversationUserDomainService.isFollowing(followerIds[i], conversationId)) {
                throw new IllegalArgumentException("Follow already exists");
            }

            // ✅ Conversion du tableau en liste
            conversationUserRepository.Follow(
                    Arrays.asList(followerIds[i]), // conversion ici
                    conversationId,
                    role
            );
        }
    }
}
