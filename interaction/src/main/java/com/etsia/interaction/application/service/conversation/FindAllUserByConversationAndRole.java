package com.etsia.interaction.application.service.conversation;

import com.etsia.common.domain.model.ConversationUserDto;
import com.etsia.common.domain.model.sub.ConversationRole;
import com.etsia.interaction.domain.repository.conversation.ConversationUserRepository;
import com.etsia.interaction.domain.service.conversation.FollowConversationUserDomainService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FindAllUserByConversationAndRole {

    private final ConversationUserRepository conversationUserRepository;
    private final FollowConversationUserDomainService followConversationUserDomainService;

    public FindAllUserByConversationAndRole(ConversationUserRepository conversationUserRepository, FollowConversationUserDomainService followConversationUserDomainService) {
        this.conversationUserRepository = conversationUserRepository;
        this.followConversationUserDomainService = followConversationUserDomainService;
    }

    public List<ConversationUserDto> execute(Integer ConversationId, ConversationRole role) {
        return conversationUserRepository.FindAllUserByConversationAndRole(ConversationId, role);
    }
}
