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
        ConversationDto conversationDto = Mapper.toConversationDto(conversation);
        return Optional.of(conversationDto);
        //return jpaConversationRepository.findById(id)
        //        .map(Mapper::toConversationDto);
    }

    @Override
    public boolean ExistsById(Integer id) {
        return jpaConversationRepository.existsById(id);
    }

    @Override
    public ConversationDto Save(CreateConversationDto conversationDto) {
        Conversation conversation = Conversation.builder()
                .title(conversationDto.getTitle())
                .description(conversationDto.getDescription())
                .type(conversationDto.getType())
                .build();
        Conversation savedConversation = jpaConversationRepository.save(conversation);
        return Mapper.toConversationDto(savedConversation);
    }

    @Override
    public void Delete(Integer id) {
        jpaConversationRepository.deleteById(id);
    }

    @Override
    public List<ConversationDto> FindAll() {
        List<Conversation> conversations = jpaConversationRepository.findAll();
        return Mapper.toConversationDtos(conversations);
    }

    @Override
    public Optional<ConversationDto> Update(UpdateConversationDto conversationDto, Integer conversationId) {
        Optional<Conversation> optionalConversation = jpaConversationRepository.findById(conversationId);

        if (optionalConversation.isEmpty()) {
            return Optional.empty();
        }

        Conversation conversation = optionalConversation.get();
        conversation.setTitle(conversationDto.getTitle());
        conversation.setDescription(conversationDto.getDescription());
        conversation.setType(conversationDto.getType());

        Conversation updatedConversation = jpaConversationRepository.save(conversation);
        return Optional.of(Mapper.toConversationDto(updatedConversation));
    }

}
