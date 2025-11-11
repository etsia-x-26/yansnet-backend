package com.etsia.interaction.domain.service.conversation;

import com.etsia.common.domain.model.ConversationDto;
import com.etsia.interaction.domain.repository.conversation.ConversationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("conversation_domain_service")
@AllArgsConstructor
public class ConversationDomainService {

    private final ConversationRepository conversationRepository;

    public boolean ExistsById(Integer id){
        return conversationRepository.ExistsById(id);
    }
}
