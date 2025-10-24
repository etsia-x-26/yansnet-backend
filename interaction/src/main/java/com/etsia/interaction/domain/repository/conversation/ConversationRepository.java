package com.etsia.interaction.domain.repository.conversation;

import com.etsia.common.domain.model.ConversationDto;
import com.etsia.interaction.domain.model.conversation.CreateConversationDto;
import com.etsia.interaction.domain.model.conversation.UpdateConversationDto;

import java.util.List;
import java.util.Optional;

public interface ConversationRepository {
    Optional<ConversationDto> FindById(Integer id);
    boolean ExistsById(Integer id);
    ConversationDto Save(CreateConversationDto conversationDto);
    void Delete(Integer id);
    List<ConversationDto> FindAll();
    Optional<ConversationDto> Update(UpdateConversationDto conversationDto, Integer ConversationId);
}
