package com.etsia.interaction.infrastructure.adapters.conversation;

import com.etsia.common.domain.model.ConversationUserDto;
import com.etsia.common.domain.model.sub.ConversationRole;
import com.etsia.common.infrastructure.entities.ConversationUser;
import com.etsia.interaction.domain.repository.conversation.ConversationUserRepository;
import com.etsia.interaction.infrastructure.repository.conversation.JpaConversationUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@AllArgsConstructor
public class FollowConversationUserRepositoryImpl implements ConversationUserRepository {

    private final JpaConversationUserRepository jpaConversationUserRepository;


    @Override
    public Optional<ConversationUserDto> FindById(Integer Id) {
        ConversationUser conversation_user = jpaConversationUserRepository.findById(Id).get();
        return null;
    }

    @Override
    public void Follow(Integer[] FollowerId, Integer ConversationId, ConversationRole role) {
        ConversationUser conversation_user = new ConversationUser();
//        conversation_user.setConversation
    }

    @Override
    public void Unfollow(Integer FollowerId, Integer ConversationId) {

    }

    @Override
    public boolean isFollowing(Integer FollowerId, Integer ConversationId) {
        return false;
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
