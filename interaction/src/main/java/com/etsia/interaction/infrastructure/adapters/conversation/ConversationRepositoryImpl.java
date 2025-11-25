package com.etsia.interaction.infrastructure.adapters.conversation;

import com.etsia.common.domain.model.ConversationDto;
import com.etsia.common.domain.model.sub.ConversationType;
import com.etsia.common.infrastructure.entities.Conversation;
import com.etsia.interaction.domain.model.conversation.CreateConversationDto;
import com.etsia.interaction.domain.model.conversation.UpdateConversationDto;
import com.etsia.interaction.domain.repository.conversation.ConversationRepository;
import com.etsia.interaction.infrastructure.repository.conversation.JpaConversationRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("conversation_impl")
public class ConversationRepositoryImpl implements ConversationRepository {

    private final JpaConversationRepository jpaConversationRepository;

    public ConversationRepositoryImpl(  @Qualifier("conversation_rep") JpaConversationRepository jpaConversationRepository) {
        this.jpaConversationRepository = jpaConversationRepository;
    }


    @Override
    public Optional<ConversationDto> FindById(Integer id) {
        Conversation conversation = jpaConversationRepository.findById(id).get();
        System.out.println("voici le conversation : "+conversation);
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
        try{
            System.out.println("voici le dto : "+conversationDto);
            Conversation conversation = new Conversation();
            conversation.setTitle(conversationDto.getTitle());
            conversation.setDescription(conversationDto.getDescription());
            conversation.setType(conversationDto.getType());
            System.out.println("voici le conversation : "+conversation);
            Conversation savedConversation = jpaConversationRepository.save(conversation);
            return Mapper.toConversationDto(savedConversation);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void Delete(Integer id) {
        jpaConversationRepository.deleteById(id);
    }

    @Override
    public List<ConversationDto> FindAll() {
        List<Conversation> conversations = jpaConversationRepository.findAll();
        System.out.println("voici ce que j'ai : "+conversations);
        return Mapper.toConversationDtos(conversations);
    }

    @Override
    public ConversationDto Update(UpdateConversationDto conversationDto, Integer conversationId) {
        Conversation optionalConversation = jpaConversationRepository.findById(conversationId).orElseThrow(() -> new RuntimeException("User not found"));

        System.out.println("voici le dto : "+optionalConversation);

        if(conversationDto.getTitle() != null){
            optionalConversation.setTitle(conversationDto.getTitle());
        }

        if(conversationDto.getDescription() != null){
            optionalConversation.setDescription(conversationDto.getDescription());
        }

        if(conversationDto.getType() != null){
            optionalConversation.setType(conversationDto.getType());
        }



        System.out.println("voici le conversation : "+optionalConversation);
        Conversation updatedConversation = jpaConversationRepository.save(optionalConversation);
        return Mapper.toConversationDto(updatedConversation);
    }

}
