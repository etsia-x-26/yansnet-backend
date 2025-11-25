package com.etsia.interaction.domain.repository.conversation;

import com.etsia.common.domain.model.ConversationUserDto;
import com.etsia.common.domain.model.sub.ConversationRole;
import com.etsia.common.domain.model.sub.ConversationRoleRenew;

import java.util.List;
import java.util.Optional;

public interface ConversationUserRepository {
    Optional<ConversationUserDto> FindById(Integer Id);
    void Follow(List<Integer> FollowerId, Integer ConversationId, ConversationRole role);
    void Unfollow(Integer FollowerId, Integer ConversationId);
    boolean isFollowing(Integer FollowerId, Integer ConversationId);

}
