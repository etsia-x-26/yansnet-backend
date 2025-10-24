package com.etsia.interaction.infrastructure.adapters.conversation;

import com.etsia.common.domain.model.ConversationDto;
import com.etsia.common.infrastructure.entities.Conversation;
import com.etsia.interaction.domain.model.conversation.CreateConversationDto;
import com.etsia.interaction.domain.model.conversation.UpdateConversationDto;
import com.etsia.interaction.domain.repository.conversation.ConversationRepository;
import com.etsia.interaction.infrastructure.repository.conversation.JpaConversationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class ConversationRepositoryImpl implements ConversationRepository {

    private final JpaConversationRepository jpaConversationRepository;


    @Override
    public Optional<ConversationDto> FindById(Integer id) {
        Conversation conversation = jpaConversationRepository.findById(id).get();
        return Optional.ofNullable(null);
    }

    @Override
    public boolean ExistsById(Integer id) {
        Conversation conversation = jpaConversationRepository.findById(id).get();
        if (conversation != null) return true;
        else return false;
    }

    @Override
    public ConversationDto Save(CreateConversationDto conversationDto) {
        Conversation conversation = new Conversation();
        conversation.setTitle(conversationDto.getTitle());
        conversation.setDescription(conversationDto.getDescription());
        conversation.setType(conversationDto.getType());

        Conversation conversation_save = jpaConversationRepository.save(conversation);
        return null;

    }

    @Override
    public void Delete(Integer id) {
        jpaConversationRepository.deleteById(id);
    }

    @Override
    public List<ConversationDto> FindAll() {
        Conversation conversation = (Conversation) jpaConversationRepository.findAll();
        return null;
    }

    @Override
    public Optional<ConversationDto> Update(UpdateConversationDto conversationDto, Integer ConversationId) {
        Conversation conversation = new Conversation();
        conversation.setId(ConversationId);
        conversation.setTitle(conversationDto.getTitle());
        conversation.setDescription(conversationDto.getDescription());
        conversation.setType(conversationDto.getType());
        Conversation conversation_update = jpaConversationRepository.save(conversation);
        return null;
    }
}
