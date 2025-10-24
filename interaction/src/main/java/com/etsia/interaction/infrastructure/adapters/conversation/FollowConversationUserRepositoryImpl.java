package com.etsia.interaction.infrastructure.adapters.conversation;

import com.etsia.common.domain.model.ConversationUserDto;
import com.etsia.common.domain.model.sub.ConversationRole;
import com.etsia.common.infrastructure.entities.Conversation;
import com.etsia.common.infrastructure.entities.ConversationUser;
import com.etsia.common.infrastructure.entities.User;
import com.etsia.interaction.domain.repository.conversation.ConversationUserRepository;
import com.etsia.interaction.infrastructure.repository.conversation.JpaConversationRepository;
import com.etsia.interaction.infrastructure.repository.conversation.JpaConversationUserRepository;
import com.etsia.interaction.infrastructure.repository.conversation.JpaUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.List;


import java.util.Optional;

import static com.etsia.common.infrastructure.config.Mapper.toConversationEntity;

@Repository
@AllArgsConstructor
public class FollowConversationUserRepositoryImpl implements ConversationUserRepository {

    private final JpaConversationUserRepository jpaConversationUserRepository;
    private final JpaUserRepository jpaUserRepository;
    private final JpaConversationRepository jpaConversationRepository;


    @Override
    public Optional<ConversationUserDto> FindById(Integer Id) {
        ConversationUser conversation_user = jpaConversationUserRepository.findById(Id).get();
        return toConversationEntity(conversation_user);
    }

    @Override
    public void Follow(List<Integer> followerIds, Integer conversationId, ConversationRole role) {
        try {
            Conversation conversation = jpaConversationRepository.findById(conversationId)
                    .orElseThrow(() -> new IllegalArgumentException("Conversation not found"));

            for (Integer followerId : followerIds) {
                User user = jpaUserRepository.findById(followerId)
                        .orElseThrow(() -> new IllegalArgumentException("User with ID " + followerId + " not found"));

                ConversationUser conversationUser = new ConversationUser();
                    conversationUser.setConversation(conversation);
                    conversationUser.setUser(user);
                    conversationUser.setRole(role);

                jpaConversationUserRepository.save(conversationUser);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("An error occurred while adding followers: " + e.getMessage());
        }
    }


    @Override
    public void Unfollow(Integer FollowerId, Integer ConversationId) {
        Conversation conversation = jpaConversationRepository.findById(ConversationId);

    }

    @Override
    public boolean isFollowing(Integer FollowerId, Integer ConversationId) {
        return jpaConversationUserRepository.existsByConversationIdAndUserId(ConversationId, FollowerId);
    }

    @Override
    public void Delete(Integer Id) {
        jpaConversationUserRepository.deleteById(Id);
    }

    @Override
    public Iterable<ConversationUserDto> FindAll() {
        return null;
    }

    @Override
    public Optional<ConversationUserDto> Update(ConversationUserDto conversationUserDto) {
        return Optional.empty();
    }

    @Override
    public Optional<ConversationUserDto> FindByUserId(Integer userId) {
        return Optional.empty();
    }

    @Override
    public Optional<ConversationUserDto> FindByConversationId(Integer conversationId) {
        return Optional.empty();
    }

    @Override
    public Optional<ConversationUserDto> FindByUserIdAndConversationId(Integer userId, Integer conversationId) {
//        ConversationUser conversation_user = jpaConversationUserRepository.findBy
        return null;
    }
}
