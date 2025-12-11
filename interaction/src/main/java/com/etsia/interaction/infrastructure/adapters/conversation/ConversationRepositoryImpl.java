package com.etsia.interaction.infrastructure.adapters.conversation;

import com.etsia.common.domain.model.ConversationDto;
import com.etsia.common.domain.model.sub.ConversationType;
import com.etsia.common.infrastructure.entities.Conversation;
import com.etsia.common.infrastructure.entities.User;
import com.etsia.interaction.domain.model.conversation.CreateConversationDto;
import com.etsia.interaction.domain.model.conversation.UpdateConversationDto;
import com.etsia.interaction.domain.repository.conversation.ConversationRepository;
import com.etsia.interaction.infrastructure.repository.conversation.JpaConversationRepository;
import com.etsia.interaction.infrastructure.repository.conversation.JpaConversationUserRepository;
import com.etsia.interaction.infrastructure.repository.conversation.JpaInteractionUserRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("conversation_impl")
public class ConversationRepositoryImpl implements ConversationRepository {

    private final JpaConversationRepository jpaConversationRepository;
    private final JpaInteractionUserRepository jpaInteractionUserRepository;
    private final JpaConversationUserRepository jpaConversationUserRepository;

    public ConversationRepositoryImpl(
            @Qualifier("conversation_rep") JpaConversationRepository jpaConversationRepository,
            JpaInteractionUserRepository jpaInteractionUserRepository,
            JpaConversationUserRepository jpaConversationUserRepository) {
        this.jpaConversationRepository = jpaConversationRepository;
        this.jpaInteractionUserRepository = jpaInteractionUserRepository;
        this.jpaConversationUserRepository = jpaConversationUserRepository;
    }


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
        try{

            // Pour les conversations PRIVATE, générer automatiquement le titre
            // en utilisant le username de l'utilisateur à l'index 1 dans la liste des followers
            String title = conversationDto.getTitle();
            if (conversationDto.getType() == ConversationType.PRIVATE 
                && conversationDto.getFollowerRoles() != null 
                && conversationDto.getFollowerRoles().size() >= 2) {
                
                // Récupérer l'utilisateur à l'index 1 (deuxième utilisateur dans la liste)
                List<Integer> followerIds = new ArrayList<>(conversationDto.getFollowerRoles().keySet());
                if (followerIds.size() > 1) {
                    Integer secondUserId = followerIds.get(1);
                    User secondUser = jpaInteractionUserRepository.findById(secondUserId)
                            .orElseThrow(() -> new IllegalArgumentException("User with id " + secondUserId + " not found"));
                    
                    // Utiliser le username de l'utilisateur
                    title = secondUser.getUsername();
                }
            }
            
            Conversation conversation = new Conversation();
            conversation.setTitle(title);
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
    @Transactional
    public void Delete(Integer id) {
        // Avant de supprimer une conversation, supprimer d'abord les entrées dans la table conversation_users.
        try {
            // Supprimer toutes les associations utilisateurs-conversation pour cette conversation
            jpaConversationUserRepository.deleteByConversationId(id);

            // Ensuite, supprimer la conversation elle-même
            jpaConversationRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Impossible de supprimer la conversation avec id " + id + " : " + e.getMessage(), e);
        }
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
