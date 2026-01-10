package com.etsia.interaction.application.service.conversation;

import com.etsia.interaction.domain.model.conversation.FindAllUserByConversationDto;
import com.etsia.interaction.domain.repository.conversation.ConversationUserRepository;
import com.etsia.interaction.domain.service.conversation.FollowConversationUserDomainService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FindAllUserByConversation {

    private final ConversationUserRepository conversationUserRepository;
    private final FollowConversationUserDomainService followConversationUserDomainService;

    public FindAllUserByConversation(ConversationUserRepository conversationUserRepository, FollowConversationUserDomainService followConversationUserDomainService) {
        this.conversationUserRepository = conversationUserRepository;
        this.followConversationUserDomainService = followConversationUserDomainService;
    }

    public List<FindAllUserByConversationDto> execute(Integer ConversationId) {
        return conversationUserRepository.FindAllUserByConversation(ConversationId);
    }

    public FindAllUserByConversationDto executeSingle(Integer ConversationId) {
        List<FindAllUserByConversationDto> results = conversationUserRepository.FindAllUserByConversation(ConversationId);
        if (results.isEmpty()) {
            throw new IllegalArgumentException("Conversation not found or has no users: " + ConversationId);
        }
        return results.get(0);
    }
}
