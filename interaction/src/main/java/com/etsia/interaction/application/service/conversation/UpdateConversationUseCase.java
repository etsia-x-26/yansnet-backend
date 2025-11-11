package com.etsia.interaction.application.service.conversation;

import com.etsia.common.domain.model.ConversationDto;
import com.etsia.interaction.domain.model.conversation.UpdateConversationDto;
import com.etsia.interaction.domain.repository.conversation.ConversationRepository;
import com.etsia.interaction.domain.service.conversation.ConversationDomainService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UpdateConversationUseCase {

    private final ConversationRepository conversationRepository;

    @Qualifier("conversation_domain_service")
    private final ConversationDomainService conversationDomainService;

    public UpdateConversationUseCase(ConversationRepository conversationRepository, @Qualifier("conversation_domain_service") ConversationDomainService conversationDomainService) {
        this.conversationRepository = conversationRepository;
        this.conversationDomainService = conversationDomainService;
    }

    public Optional<ConversationDto> execute(UpdateConversationDto conversationDto, Integer ConversationId){
        if (!conversationDomainService.ExistsById(ConversationId)){
            throw new IllegalArgumentException("Conversation Doesn't exist");
        }
        return conversationRepository.Update(conversationDto,ConversationId);
    }
}
