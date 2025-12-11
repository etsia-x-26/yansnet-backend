package com.etsia.interaction.application.service.conversation;

import java.util.List;

import com.etsia.common.domain.model.ConversationUserDto;
import com.etsia.interaction.domain.model.conversation.FindAllConversationByUserDto;
import com.etsia.interaction.domain.repository.conversation.ConversationUserRepository;
import com.etsia.interaction.domain.service.conversation.FollowConversationUserDomainService;
import org.springframework.stereotype.Service;

@Service
public class FindAllConversationByUser {

    private final ConversationUserRepository conversationUserRepository;
    private final FollowConversationUserDomainService followConversationUserDomainService;

    public FindAllConversationByUser(ConversationUserRepository conversationUserRepository,
            FollowConversationUserDomainService followConversationUserDomainService) {
        this.conversationUserRepository = conversationUserRepository;
        this.followConversationUserDomainService = followConversationUserDomainService;
    }

    public List<FindAllConversationByUserDto> execute(Integer FollowerId) {
        return conversationUserRepository.FindAllConversationByUser(FollowerId);
    }
}
