package com.etsia.interaction.application.service.conversation;

import com.etsia.interaction.domain.repository.conversation.ConversationRepository;
import com.etsia.interaction.domain.service.conversation.ConversationDomainService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DeleteConversationUseCase {

    private final ConversationRepository conversationRepository;
    private final ConversationDomainService conversationDomainService;

    public void execute(Integer ConversationId){
        if (!conversationDomainService.ExistsById(ConversationId)){
            throw new IllegalArgumentException("Conversation Doesn't exist");
        }
        conversationRepository.Delete(ConversationId);
    }
}
