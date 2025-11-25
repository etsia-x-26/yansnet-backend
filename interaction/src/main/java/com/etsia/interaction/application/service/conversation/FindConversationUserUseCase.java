package com.etsia.interaction.application.service.conversation;

import com.etsia.common.domain.model.ConversationUserDto;
import com.etsia.interaction.domain.repository.conversation.ConversationUserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FindConversationUserUseCase {

    private final ConversationUserRepository conversationUserRepository;

    public FindConversationUserUseCase(ConversationUserRepository conversationUserRepository) {
        this.conversationUserRepository = conversationUserRepository;
    }

    public Optional<ConversationUserDto> execute(Integer Id){
        return conversationUserRepository.FindById(Id);
    }
}
