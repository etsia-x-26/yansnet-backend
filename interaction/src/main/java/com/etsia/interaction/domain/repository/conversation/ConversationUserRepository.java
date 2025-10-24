package com.etsia.interaction.domain.repository.conversation;

import com.etsia.common.domain.model.ConversationUserDto;
import com.etsia.common.domain.model.sub.ConversationRole;

import java.util.Optional;

public interface ConversationUserRepository {
    Optional<ConversationUserDto> FindById(Integer Id);
    void Follow(Integer[] FollowerId, Integer ConversationId, ConversationRole role);
    void Unfollow(Integer FollowerId, Integer ConversationId);
    boolean isFollowing(Integer FollowerId, Integer ConversationId);
    void Delete(Integer Id);
    Iterable<ConversationUserDto> FindAll();
    Optional<ConversationUserDto> Update(ConversationUserDto conversationUserDto);
    Optional<ConversationUserDto> FindByUserId(Integer userId);
    Optional<ConversationUserDto> FindByConversationId(Integer conversationId);
    Optional<ConversationUserDto> FindByUserIdAndConversationId(Integer userId, Integer conversationId);
}
