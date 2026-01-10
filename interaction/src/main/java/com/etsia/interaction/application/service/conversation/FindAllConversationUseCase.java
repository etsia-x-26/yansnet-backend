package com.etsia.interaction.application.service.conversation;

import com.etsia.common.domain.model.ConversationDto;
import com.etsia.interaction.domain.repository.conversation.ConversationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class FindAllConversationUseCase {

    private final ConversationRepository conversationRepository;

    public List<ConversationDto> execute() {
        try {
            System.out.println("Find All service");
            return conversationRepository.FindAll();
        } catch (Exception e) {
            System.out.println(e);
            throw new IllegalArgumentException("Follow already exists");
        }
    }
}
