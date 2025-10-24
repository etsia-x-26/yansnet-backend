package com.etsia.interaction.application.service.conversation;

import com.etsia.common.domain.model.ConversationDto;
import com.etsia.interaction.domain.repository.conversation.ConversationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class FindConversationUseCase {

    private final ConversationRepository conversationRepository;

    public Optional<ConversationDto> execute(Integer ConversationId){
        try{
            return conversationRepository.FindById(ConversationId);
        } catch(Exception e) {
            throw new IllegalArgumentException("Follow already exists");
    }
    }
}
