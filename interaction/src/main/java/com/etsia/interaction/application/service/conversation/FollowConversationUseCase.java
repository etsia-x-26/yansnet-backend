package com.etsia.interaction.application.service.conversation;

import com.etsia.common.domain.model.sub.ConversationRole;
import com.etsia.interaction.domain.repository.conversation.ConversationUserRepository;
import com.etsia.interaction.domain.service.conversation.FollowConversationUserDomainService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class FollowConversationUseCase {

    private final ConversationUserRepository conversationUserRepository;
    private final FollowConversationUserDomainService followConversationUserDomainService;

    public void execute(Integer[] FollowerIds, Integer ConversationId, ConversationRole role){
        for (int i = 0; i < FollowerIds.length; i++){
            if (followConversationUserDomainService.isFollowing(FollowerIds[i], ConversationId)){
                throw new IllegalArgumentException("Follow already exists");
            }
            conversationUserRepository.Follow(new Integer[]{FollowerIds[i]}, ConversationId, role);
        }
    }
}
