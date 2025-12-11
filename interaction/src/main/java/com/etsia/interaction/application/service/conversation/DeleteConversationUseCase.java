package com.etsia.interaction.application.service.conversation;

import com.etsia.interaction.domain.repository.conversation.ConversationRepository;
import com.etsia.interaction.domain.service.conversation.ConversationDomainService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class DeleteConversationUseCase {

    private final ConversationRepository conversationRepository;
    @Qualifier("conversation_domain_service")
    private final ConversationDomainService conversationDomainService;

    public DeleteConversationUseCase(ConversationRepository conversationRepository, @Qualifier("conversation_domain_service") ConversationDomainService conversationDomainService) {
        this.conversationRepository = conversationRepository;
        this.conversationDomainService = conversationDomainService;
    }

    public void execute(Integer ConversationId){
        if (!conversationDomainService.ExistsById(ConversationId)){
            throw new IllegalArgumentException("Conversation Doesn't exist");
        }
        conversationRepository.Delete(ConversationId);
    }
}
