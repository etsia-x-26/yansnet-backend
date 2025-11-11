package com.etsia.interaction.application.service.conversation;

import com.etsia.common.domain.model.ConversationDto;
import com.etsia.interaction.domain.model.conversation.CreateConversationDto;
import com.etsia.interaction.domain.repository.conversation.ConversationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CreateConversationUseCase {

    private final ConversationRepository conversationRepository;

    public ConversationDto execute(CreateConversationDto conversationDto) {
        try {
            System.out.println("service");
            return conversationRepository.Save(conversationDto);
        } catch (Exception e) {
            throw new IllegalArgumentException("Follow already exists");
        }
    }
}
