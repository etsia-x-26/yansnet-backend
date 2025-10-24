package com.etsia.interaction.application.service.conversation;

import com.etsia.common.domain.model.ConversationDto;
import com.etsia.interaction.domain.repository.conversation.ConversationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FindAllConversationUseCase {

    private final ConversationRepository conversationRepository;

    public ConversationDto[] execute(){
        try{
            return conversationRepository.FindAll();
        } catch(Exception e){
            throw RessourceNotFoundException("Conversation Not Found")
        }
    }
}
