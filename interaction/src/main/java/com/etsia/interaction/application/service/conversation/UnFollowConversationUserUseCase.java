package com.etsia.interaction.application.service.conversation;

import com.etsia.interaction.domain.repository.conversation.ConversationUserRepository;
import com.etsia.interaction.domain.service.conversation.FollowConversationUserDomainService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UnFollowConversationUserUseCase {

    private final ConversationUserRepository conversationUserRepository;
    private final FollowConversationUserDomainService followConversationUserDomainService;

    public void execute(Integer FollowerId, Integer ConversationId){
        if (!followConversationUserDomainService.isFollowing(FollowerId, ConversationId)){
            throw new IllegalArgumentException("Follow does not exist");
        }
        conversationUserRepository.Unfollow(FollowerId,ConversationId);
    }


}
