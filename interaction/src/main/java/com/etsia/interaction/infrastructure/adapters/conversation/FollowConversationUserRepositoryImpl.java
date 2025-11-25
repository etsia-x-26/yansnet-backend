package com.etsia.interaction.infrastructure.adapters.conversation;

import com.etsia.common.domain.model.ConversationUserDto;
import com.etsia.common.domain.model.sub.ConversationRole;
import com.etsia.common.infrastructure.entities.Conversation;
import com.etsia.common.infrastructure.entities.ConversationUser;
import com.etsia.common.infrastructure.entities.User;
import com.etsia.interaction.domain.repository.conversation.ConversationUserRepository;
import com.etsia.interaction.infrastructure.repository.conversation.JpaConversationRepository;
import com.etsia.interaction.infrastructure.repository.conversation.JpaConversationUserRepository;
import com.etsia.interaction.infrastructure.repository.conversation.JpaInteractionUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.List;


import java.util.Optional;

@Repository
@AllArgsConstructor
public class FollowConversationUserRepositoryImpl implements ConversationUserRepository {

    private final JpaConversationUserRepository jpaConversationUserRepository;
    private final JpaInteractionUserRepository jpaInteractionUserRepository;
    private final JpaConversationRepository jpaConversationRepository;


    @Override
    public Optional<ConversationUserDto> FindById(Integer Id) {
        try {
            Optional<ConversationUser> conversationUserOpt = jpaConversationUserRepository.findByIdWithRelations(Id);
            if (conversationUserOpt.isEmpty()) {
                return Optional.empty();
            }
            ConversationUser conversationUser = conversationUserOpt.get();
            return Optional.of(Mapper.toConversationUserDto(conversationUser));
        } catch (Exception e) {
            throw new RuntimeException("Error finding ConversationUser with id " + Id + ": " + e.getMessage(), e);
        }
    }

    @Override
    public void Follow(List<Integer> followerIds, Integer conversationId, ConversationRole role) {
        try {
            Conversation conversation = jpaConversationRepository.findById(conversationId)
                    .orElseThrow(() -> new IllegalArgumentException("Conversation not found"));

            for (Integer followerId : followerIds) {
                User user = jpaInteractionUserRepository.findById(followerId)
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
        Conversation conversation = jpaConversationRepository.findById(ConversationId)
            .orElseThrow(() -> new IllegalArgumentException("Conversation not found"));
        User user = jpaInteractionUserRepository.findById(FollowerId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
    
    ConversationUser conversationUser = jpaConversationUserRepository
            .findByConversationIdAndUserId(conversation.getId(), user.getId())
            .orElseThrow(() -> new IllegalArgumentException("ConversationUser relationship not found"));
    
    jpaConversationUserRepository.delete(conversationUser);
}

    @Override
    public boolean isFollowing(Integer FollowerId, Integer ConversationId) {
        return jpaConversationUserRepository.existsByConversationIdAndUserId(ConversationId, FollowerId);
    }


}
