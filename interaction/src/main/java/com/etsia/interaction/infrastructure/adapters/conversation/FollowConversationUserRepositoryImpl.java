package com.etsia.interaction.infrastructure.adapters.conversation;

import com.etsia.common.domain.model.ConversationDto;
import com.etsia.common.domain.model.ConversationUserDto;
import com.etsia.common.domain.model.UserDto;
import com.etsia.common.domain.model.sub.ConversationRole;
import com.etsia.common.infrastructure.config.Mapper;
import com.etsia.common.infrastructure.entities.Conversation;
import com.etsia.common.infrastructure.entities.ConversationUser;
import com.etsia.common.infrastructure.entities.User;
import com.etsia.interaction.domain.model.conversation.FindAllConversationByUserDto;
import com.etsia.interaction.domain.model.conversation.FindAllUserByConversationDto;
import com.etsia.interaction.domain.model.conversation.UserWithRoleDto;
import com.etsia.interaction.domain.repository.conversation.ConversationUserRepository;
import com.etsia.interaction.infrastructure.repository.conversation.JpaConversationRepository;
import com.etsia.interaction.infrastructure.repository.conversation.JpaConversationUserRepository;
import com.etsia.interaction.infrastructure.repository.JpaInteractionUserRepository;
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
    public List<FindAllConversationByUserDto> FindAllConversationByUser(Integer UserId) {
        try {
            List<ConversationUser> conversationUsers = jpaConversationUserRepository.findByUserIdWithRelations(UserId);
            
            if (conversationUsers.isEmpty()) {
                return List.of();
            }
            
            // All ConversationUser entities have the same user (filtered by userId)
            User user = conversationUsers.get(0).getUser();
            List<ConversationDto> conversations = conversationUsers.stream()
                    .map(ConversationUser::getConversation)
                    .map(Mapper::toConversationDto)
                    .toList();
            
            FindAllConversationByUserDto result = new FindAllConversationByUserDto(
                    Mapper.toUserDto(user),
                    conversations
            );
            
            return List.of(result);
        } catch (Exception e) {
            throw new RuntimeException("Error finding conversations for user with id " + UserId + ": " + e.getMessage(),
                    e);
        }
    }

    @Override
    public List<FindAllUserByConversationDto> FindAllUserByConversation(Integer ConversationId) {
        try {
            List<ConversationUser> conversationUsers = jpaConversationUserRepository
                    .findByConversationIdWithRelations(ConversationId);
            
            if (conversationUsers.isEmpty()) {
                return List.of();
            }
            
            // All ConversationUser entities have the same conversation (filtered by conversationId)
            Conversation conversation = conversationUsers.get(0).getConversation();
            
            // Créer une liste d'utilisateurs avec leur rôle respectif
            List<UserWithRoleDto> usersWithRoles = conversationUsers.stream()
                    .map(cu -> new UserWithRoleDto(
                            Mapper.toUserDto(cu.getUser()),
                            cu.getRole()
                    ))
                    .toList();
            
            // Use the first ConversationUser's id
            ConversationUser first = conversationUsers.get(0);
            
            FindAllUserByConversationDto result = new FindAllUserByConversationDto(
                    first.getId(),
                    Mapper.toConversationDto(conversation),
                    usersWithRoles
            );
            
            return List.of(result);
        } catch (Exception e) {
            throw new RuntimeException(
                    "Error finding users for conversation with id " + ConversationId + ": " + e.getMessage(), e);
        }
    }

    @Override
    public List<ConversationUserDto> FindAllUserByConversationAndRole(Integer ConversationId, ConversationRole role) {
        try {
            List<ConversationUser> conversationUsers = jpaConversationUserRepository
                    .findByConversationIdAndRoleWithRelations(ConversationId, role);
            return conversationUsers.stream()
                    .map(Mapper::toConversationUserDto)
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Error finding users for conversation with id " + ConversationId + " and role "
                    + role + ": " + e.getMessage(), e);
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
