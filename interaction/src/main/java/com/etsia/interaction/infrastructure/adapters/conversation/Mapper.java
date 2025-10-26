package com.etsia.interaction.infrastructure.adapters.conversation;

import com.etsia.common.domain.model.ConversationDto;
import com.etsia.common.domain.model.ConversationUserDto;
import com.etsia.common.domain.model.UserDto;
import com.etsia.common.infrastructure.entities.Conversation;
import com.etsia.common.infrastructure.entities.ConversationUser;
import com.etsia.common.infrastructure.entities.User;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Mapper {

    // ========================
    // CONVERSATION
    // ========================

    public static ConversationDto toConversationDto(Conversation entity) {
        if (entity == null) return null;
        return ConversationDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .type(entity.getType())
                .build();
    }

    public static Conversation toConversationEntity(ConversationDto dto) {
        if (dto == null) return null;
        Conversation conversation = new Conversation();
        conversation.setId(dto.getId());
        conversation.setTitle(dto.getTitle());
        conversation.setDescription(dto.getDescription());
        conversation.setType(dto.getType());
        return conversation;
    }

    public static List<ConversationDto> toConversationDtos(List<Conversation> entities) {
        if (entities == null) return Collections.emptyList();
        return entities.stream()
                .map(Mapper::toConversationDto)
                .collect(Collectors.toList());
    }

    // ========================
    // CONVERSATION USER
    // ========================

    public static ConversationUserDto toConversationUserDto(ConversationUser entity) {
        if (entity == null) return null;
        return new ConversationUserDto(
                entity.getId(),
                toConversationDto(entity.getConversation()),
                toUserDto(entity.getUser()),
                entity.getRole()
        );
    }

    public static ConversationUser toConversationUserEntity(ConversationUserDto dto) {
        if (dto == null) return null;
        ConversationUser conversationUser = new ConversationUser();
        conversationUser.setId(dto.getId());
        conversationUser.setConversation(toConversationEntity(dto.getConversation()));
        conversationUser.setUser(toUserEntity(dto.getUser()));
        conversationUser.setRole(dto.getRole());
        return conversationUser;
    }

    public static List<ConversationUserDto> toConversationUserDtos(List<ConversationUser> entities) {
        if (entities == null) return Collections.emptyList();
        return entities.stream()
                .map(Mapper::toConversationUserDto)
                .collect(Collectors.toList());
    }

    public static List<ConversationUser> toConversationUserEntities(List<ConversationUserDto> dtos) {
        if (dtos == null) return Collections.emptyList();
        return dtos.stream()
                .map(Mapper::toConversationUserEntity)
                .collect(Collectors.toList());
    }

    // ========================
    // USER (simplifié)
    // ========================

    // ⚠️ À adapter selon ton entité User complète
    public static UserDto toUserDto(User entity) {
        if (entity == null) return null;
        return UserDto.builder()
                .id(entity.getId())
                .password(entity.getPassword())
                .isActive(entity.getIsActive())
                .isBlocked(entity.getIsBlocked())
                .category(null) // tu peux mapper si tu as UserCategoryDto
                .department(null)
                .batch(null)
                .email(entity.getEmail())
                .phoneNumber(entity.getPhoneNumber())
                .totalFollowers(0)
                .totalFollowing(0)
                .totalPosts(0)
                .build();
    }

    public static User toUserEntity(UserDto dto) {
        if (dto == null) return null;
        User user = new User();
        user.setId(dto.getId());
        user.setPassword(dto.getPassword());
        user.setIsActive(dto.getIsActive());
        user.setIsBlocked(dto.getIsBlocked());
        user.setEmail(dto.getEmail());
        user.setPhoneNumber(dto.getPhoneNumber());
        return user;
    }
}
