package com.etsia.common.infrastructure.config;

import com.etsia.common.domain.model.*;
import com.etsia.common.infrastructure.entities.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class Mapper {

    // ========================
    // USER (simplifié)
    // ========================

    // ⚠️ À adapter selon ton entité User complète
    /**
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
     **/

    /**
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
     **/

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

    public static List<MediaDto> toMediaDtos(List<Media> entities) {
        if (entities == null) return Collections.emptyList();
        return entities.stream().map(Mapper::toMediaDto).collect(Collectors.toList());
    }

    public static MediaDto toMediaDto(Media entity) {
        if (entity == null) return null;
        return MediaDto.builder()
                .id(entity.getId())
                .url(entity.getUrl())
                .uploadedAt(entity.getUploadedAt())
                .type(entity.getType())
                .build();


    }

    public static List<MessageDto> toMessageDtos(List<Message> entities) {
        if (entities == null) return Collections.emptyList();
        return entities.stream().map(Mapper::toMessageDto).collect(Collectors.toList());
    }

    public static MessageDto toMessageDto(Message entity) {
        if (entity == null) return null;
        return MessageDto.builder()
                .id(entity.getId())
                .content(entity.getContent())
                .url(entity.getUrl())
                .type(entity.getType())
                .user(UserDto.builder()
                        .id(entity.getUser() != null ? entity.getUser().getId() : null)
                        .build())
                .conversation(ConversationDto.builder()
                        .id(entity.getConversation() != null ? entity.getConversation().getId() : null)
                        .build())
                .build();

    }

    public static List<Media> toMediaEntities(List<MediaDto> Dtos) {
        if (Dtos == null) return Collections.emptyList();
        return Dtos.stream().map(Mapper::toMediaEntity).collect(Collectors.toList());
    }

    public static Media toMediaEntity(MediaDto Dto) {
        if (Dto == null) return null;
        return Media.builder()
                .id(Dto.getId())
                .url(Dto.getUrl())
                .uploadedAt(Dto.getUploadedAt())
                .type(Dto.getType())
                .build();

    }

    public static List<Message> toMessageEntities(List<MessageDto> Dtos) {
        if (Dtos == null) return Collections.emptyList();
        return Dtos.stream().map(Mapper::toMessageEntity).collect(Collectors.toList());
    }

    public static Message toMessageEntity(MessageDto Dto) {
        if (Dto == null) return null;
        Message entity = new Message();
        entity.setId(Dto.getId());
        entity.setContent(Dto.getContent());
        entity.setUrl(Dto.getUrl());
        entity.setType(Dto.getType());
        return entity;
    }

    public static List<Conversation> toConversationEntities(List<ConversationDto> Dtos) {
        if (Dtos == null) return Collections.emptyList();
        return Dtos.stream().map(Mapper::toConversationEntity).collect(Collectors.toList());
    }

    public static List<ChannelDto> toChannelDtos(List<Channel> entities) {
        if (entities == null) return Collections.emptyList();
        return entities.stream().map(Mapper::toChannelDto).collect(Collectors.toList());
    }

    public static ChannelDto toChannelDto(Channel entity) {
        if (entity == null) return null;
        return ChannelDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .totalFollowers(entity.getTotalFollowers())
                .build();
    }

    public static List<Channel> toChannelEntities(List<ChannelDto> dtos) {
        if (dtos == null) return Collections.emptyList();
        return dtos.stream().map(Mapper::toChannelEntity).collect(Collectors.toList());
    }

    public static Channel toChannelEntity(ChannelDto dto) {
        if (dto == null) return null;
        return Channel.builder()
                .id(dto.getId())
                .description(dto.getDescription())
                .totalFollowers(dto.getTotalFollowers())
                .title(dto.getTitle())
                .build();
    }

    public static List<DepartmentDto> toDepartmentDtos(List<Department> entities) {
        if (entities == null) return Collections.emptyList();
        return entities.stream().map(Mapper::toDepartmentDto).collect(Collectors.toList());
    }

    public static DepartmentDto toDepartmentDto(Department entity) {
        if (entity == null) return null;
        return DepartmentDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }

    public static List<Department> toDepartmentEntities(List<DepartmentDto> dtos) {
        if (dtos == null) return Collections.emptyList();
        return dtos.stream().map(Mapper::toDepartmentEntity).collect(Collectors.toList());
    }

    public static Department toDepartmentEntity(DepartmentDto dto) {
        if (dto == null) return null;
        return Department.builder()
                .id(dto.getId())
                .name(dto.getName())
                .build();
    }

    public static List<CommentDto> toCommentDtos(List<Comment> entities) {
        if (entities == null) return Collections.emptyList();
        return entities.stream().map(Mapper::toCommentDto).collect(Collectors.toList());
    }

    public static CommentDto toCommentDto(Comment entity) {
        if (entity == null) return null;
        return CommentDto.builder()
                .id(entity.getId())
                .content(entity.getContent())
                .createdAt(entity.getCreatedAt())
                .user(UserDto.builder()
                        .id(entity.getUser() != null ? entity.getUser().getId() : null)
                        .build())
                .post(PostDto.builder()
                        .id(entity.getPost() != null ? entity.getPost().getId() : null)
                        .build())
                .build();
    }

    public static List<Comment> toCommentEntities(List<CommentDto> dtos) {
        if (dtos == null) return Collections.emptyList();
        return dtos.stream().map(Mapper::toCommentEntity).collect(Collectors.toList());
    }

    public static Comment toCommentEntity(CommentDto dto) {
        if (dto == null) return null;
        return Comment.builder()
                .id(dto.getId())
                .content(dto.getContent())
                .createdAt(dto.getCreatedAt())
                .user(User.builder()
                        .id(dto.getUser() != null ? dto.getUser().getId() : null)
                        .build())
                .post(Post.builder()
                        .id(dto.getPost() != null ? dto.getPost().getId() : null)
                        .build())
                .build();
    }

    public static List<CommentLikeDto> toCommentLikeDtos(List<CommentLike> entities) {
        if (entities == null) return Collections.emptyList();
        return entities.stream().map(Mapper::toCommentLikeDto).collect(Collectors.toList());
    }

    public static CommentLikeDto toCommentLikeDto(CommentLike entity) {
        if (entity == null) return null;
        return CommentLikeDto.builder()
                .id(CommentLikeIdDto.builder().commentId(entity.getComment().getId()).userId(entity.getUser().getId()).build())
                .user(UserDto.builder()
                        .id(entity.getUser() != null ? entity.getUser().getId() : null)
                        .build())
                .comment(CommentDto.builder()
                        .id(entity.getComment() != null ? entity.getComment().getId() : null)
                        .build())
                .build();
    }

    public static List<CommentLike> toCommentLikeEntities(List<CommentLikeDto> dtos) {
        if (dtos == null) return Collections.emptyList();
        return dtos.stream().map(Mapper::toCommentLikeEntity).collect(Collectors.toList());
    }

    public static CommentLike toCommentLikeEntity(CommentLikeDto dto) {
        if (dto == null) return null;
        return CommentLike.builder()
                .id(CommentLikeId.builder().commentId(dto.getComment().getId()).userId(dto.getUser().getId()).build())
                .user(User.builder()
                        .id(dto.getUser().getId())
                        .build())
                .comment(Comment.builder()
                        .id(dto.getComment().getId())
                        .build())
                .build();
    }

    public static List<ChannelFollowerDto> toChannelFollowerDtos(List<ChannelFollower> entities) {
        if (entities == null) return Collections.emptyList();
        return entities.stream().map(Mapper::toChannelFollowerDto).collect(Collectors.toList());
    }

    public static ChannelFollowerDto toChannelFollowerDto(ChannelFollower entity) {
        if (entity == null) return null;
        return ChannelFollowerDto.builder()
                .id(ChannelFollowerIdDto.builder()
                        .channelId(entity.getChannel().getId())
                        .userId(entity.getUser().getId())
                        .build())
                .user(UserDto.builder()
                        .id(entity.getUser() != null ? entity.getUser().getId() : null)
                        .build())
                .channel(ChannelDto.builder()
                        .id(entity.getChannel() != null ? entity.getChannel().getId() : null)
                        .build())
                .build();
    }

    public static List<ChannelFollower> toChannelFollowerEntities(List<ChannelFollowerDto> dtos) {
        if (dtos == null) return Collections.emptyList();
        return dtos.stream().map(Mapper::toChannelFollowerEntity).collect(Collectors.toList());
    }

    public static ChannelFollower toChannelFollowerEntity(ChannelFollowerDto dto) {
        if (dto == null) return null;
        return ChannelFollower.builder()
                .id(ChannelFollowerId.builder()
                        .channelId(dto.getChannel().getId())
                        .userId(dto.getUser().getId())
                        .build())
                .user(User.builder()
                        .id(dto.getUser().getId())
                        .build())
                .channel(Channel.builder()
                        .id(dto.getChannel().getId())
                        .build())
                .build();
    }

    public static List<BatchDto> toBatchDtos(List<Batch> entities) {
        if (entities == null) return Collections.emptyList();
        return entities.stream().map(Mapper::toBatchDto).collect(Collectors.toList());
    }

    public static BatchDto toBatchDto(Batch entity) {
        if (entity == null) return null;
        return BatchDto.builder()
                .id(entity.getId())
                .departement(entity.getDepartement())
                .endYear(entity.getEndYear())
                .build();
    }

    public static List<Batch> toBatchEntities(List<BatchDto> dtos) {
        if (dtos == null) return Collections.emptyList();
        return dtos.stream().map(Mapper::toBatchEntity).collect(Collectors.toList());
    }

    public static Batch toBatchEntity(BatchDto dto) {
        if (dto == null) return null;
        return Batch.builder()
                .id(dto.getId())
                .departement(dto.getDepartement())
                .endYear(dto.getEndYear())
                .build();
    }

    public static List<PostDto> toPostDtos(List<Post> entities) {
        if (entities == null) return Collections.emptyList();
        return entities.stream().map(Mapper::toPostDto).collect(Collectors.toList());
    }

    public static PostDto toPostDto(Post entity) {
        if (entity == null) return null;
        return PostDto.builder()
                .id(entity.getId())
                .content(entity.getContent())
                .totalComments(entity.getTotalComments())
                .totalLikes(entity.getTotalLikes())
                .deletedAt(entity.getDeletedAt())
                .createdAt(entity.getCreatedAt())
                .media(toMediaDtos(entity.getMedia().stream().toList()))
                .user(UserDto.builder()
                        .id(entity.getUser() != null ? entity.getUser().getId() : null)
                        .build())
                .channel(ChannelDto.builder()
                        .id(entity.getChannel() != null ? entity.getChannel().getId() : null)
                        .build())
                .build();
    }

    public static List<Post> toPostEntities(List<PostDto> dtos) {
        if (dtos == null) return Collections.emptyList();
        return dtos.stream().map(Mapper::toPostEntity).collect(Collectors.toList());
    }

    public static Post toPostEntity(PostDto dto) {
        if (dto == null) return null;
        return Post.builder()
                .id(dto.getId())
                .content(dto.getContent())
                .totalComments(dto.getTotalComments())
                .totalLikes(dto.getTotalLikes())
                .deletedAt(dto.getDeletedAt())
                .createdAt(dto.getCreatedAt())
                .media(new HashSet<>(( toMediaEntities(dto.getMedia()))))
                .user(dto.getChannel() != null ?User.builder()
                        .id(dto.getUser().getId())
                        .build():null)
                .channel(dto.getChannel() != null ?Channel.builder()
                        .id(dto.getChannel().getId())
                        .build():null)
                .build();
    }

    public static List<UserCategoryDto> toUserCategoryDtos(List<UserCategory> entities) {
        if (entities == null) return Collections.emptyList();
        return entities.stream().map(Mapper::toUserCategoryDto).collect(Collectors.toList());
    }

    public static UserCategoryDto toUserCategoryDto(UserCategory entity) {
        if (entity == null) return null;
        return UserCategoryDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }

    public static List<UserCategory> toUserCategoryEntities(List<UserCategoryDto> dtos) {
        if (dtos == null) return Collections.emptyList();
        return dtos.stream().map(Mapper::toUserCategoryEntity).collect(Collectors.toList());
    }

    public static UserCategory toUserCategoryEntity(UserCategoryDto dto) {
        if (dto == null) return null;
        return UserCategory.builder()
                .id(dto.getId())
                .name(dto.getName())
                .build();
    }

    public static List<UserFollowDto> toUserFollowDtos(List<UserFollow> entities) {
        if (entities == null) return Collections.emptyList();
        return entities.stream().map(Mapper::toUserFollowDto).collect(Collectors.toList());
    }

    public static UserFollowDto toUserFollowDto(UserFollow entity) {
        if (entity == null) return null;
        return UserFollowDto.builder()
                .id(UserFollowIdDto.builder()
                        .followerId(entity.getFollower().getId())
                        .followedId(entity.getFollower().getId())
                        .build())
                .follower(UserDto.builder()
                        .id(entity.getFollower() != null ? entity.getFollower().getId() : null)
                        .build())
                .followed(UserDto.builder()
                        .id(entity.getFollowed() != null ? entity.getFollowed().getId() : null)
                        .build())
                .build();
    }

    public static List<UserFollow> toUserFollowEntities(List<UserFollowDto> dtos) {
        if (dtos == null) return Collections.emptyList();
        return dtos.stream().map(Mapper::toUserFollowEntity).collect(Collectors.toList());
    }

    public static UserFollow toUserFollowEntity(UserFollowDto dto) {
        if (dto == null) return null;
        return UserFollow.builder()
                .id(UserFollowId.builder()
                        .followerId(dto.getFollower().getId())
                        .followedId(dto.getId().getFollowedId())
                        .build())
                .follower(User.builder()
                        .id(dto.getFollower().getId())
                        .build())
                .followed(User.builder()
                        .id(dto.getFollowed().getId())
                        .build())
                .build();
    }

    public static List<UserDto> toUserDtos(List<User> entities) {
        if (entities == null) return Collections.emptyList();
        return entities.stream().map(Mapper::toUserDto).collect(Collectors.toList());
    }

    public static UserDto toUserDto(User entity) {
        if (entity == null) return null;
        return UserDto.builder()
                .id(entity.getId())
                .password(entity.getPassword())
                .isActive(entity.getIsActive())
                .isBlocked(entity.getIsBlocked())
                .email(entity.getEmail())
                .phoneNumber(entity.getPhoneNumber())
                .name(entity.getName())
                .username(entity.getUsername())
                .bio(entity.getBio())
                .profilePictureUrl(entity.getProfilePictureUrl())
                .isMentor(entity.getIsMentor())
                .promotionYear(entity.getPromotionYear())
                .totalFollowers(entity.getTotalFollowers())
                .totalFollowing(entity.getTotalFollowing())
                .totalPosts(entity.getTotalPosts())
                .category(toUserCategoryDto(entity.getCategory()))
                .department(toDepartmentDto(entity.getDepartment()))
                .batch(toBatchDto(entity.getBatch()))
                .build();
    }

    public static List<User> toUserEntities(List<UserDto> dtos) {
        if (dtos == null) return Collections.emptyList();
        return dtos.stream().map(Mapper::toUserEntity).collect(Collectors.toList());
    }

    public static User toUserEntity(UserDto dto) {
        if (dto == null) return null;
        return User.builder()
                .id(dto.getId())
                .password(dto.getPassword())
                .isActive(dto.getIsActive())
                .isBlocked(dto.getIsBlocked())
                .email(dto.getEmail())
                .bio(dto.getBio())
                .profilePictureUrl(dto.getProfilePictureUrl())
                .isMentor(dto.getIsMentor())
                .promotionYear(dto.getPromotionYear())
                .totalFollowers(dto.getTotalFollowers())
                .totalFollowing(dto.getTotalFollowing())
                .totalPosts(dto.getTotalPosts())
                .category(toUserCategoryEntity(dto.getCategory()))
                .department(toDepartmentEntity(dto.getDepartment()))
                .batch(toBatchEntity(dto.getBatch()))
                .build();
    }

    public static List<GroupDto> toGroupDtos(List<Group> entities) {
        if (entities == null) return Collections.emptyList();
        return entities.stream().map(Mapper::toGroupDto).collect(Collectors.toList());
    }

    public static GroupDto toGroupDto(Group entity) {
        if (entity == null) return null;
        return GroupDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .profileImageUrl(entity.getProfileImageUrl())
                .createdBy(entity.getCreatedBy() != null ? entity.getCreatedBy().getId() : null)
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static List<Group> toGroupEntities(List<GroupDto> dtos) {
        if (dtos == null) return Collections.emptyList();
        return dtos.stream().map(Mapper::toGroupEntity).collect(Collectors.toList());
    }

    public static Group toGroupEntity(GroupDto dto) {
        if (dto == null) return null;
        return Group.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .profileImageUrl(dto.getProfileImageUrl())
                .createdBy(dto.getCreatedBy() != null ? User.builder().id(dto.getCreatedBy()).build() : null)
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }

    public static List<GroupMemberDto> toGroupMemberDtos(List<GroupMember> entities) {
        if (entities == null) return Collections.emptyList();
        return entities.stream().map(Mapper::toGroupMemberDto).collect(Collectors.toList());
    }

    public static GroupMemberDto toGroupMemberDto(GroupMember entity) {
        if (entity == null) return null;
        return GroupMemberDto.builder()
                .userId(entity.getId() != null ? entity.getId().getUserId() : null)
                .groupId(entity.getId() != null ? entity.getId().getGroupId() : null)
                .role(entity.getRole())
                .joinedAt(entity.getJoinedAt())
                .user(entity.getUser() != null ? UserDto.builder().id(entity.getUser().getId()).build() : null)
                .group(entity.getGroup() != null ? GroupDto.builder().id(entity.getGroup().getId()).build() : null)
                .build();
    }

    public static List<GroupMember> toGroupMemberEntities(List<GroupMemberDto> dtos) {
        if (dtos == null) return Collections.emptyList();
        return dtos.stream().map(Mapper::toGroupMemberEntity).collect(Collectors.toList());
    }

    public static GroupMember toGroupMemberEntity(GroupMemberDto dto) {
        if (dto == null) return null;
        return GroupMember.builder()
                .id(GroupMemberId.builder()
                        .userId(dto.getUserId())
                        .groupId(dto.getGroupId())
                        .build())
                .role(dto.getRole())
                .joinedAt(dto.getJoinedAt())
                .user(dto.getUser() != null ? User.builder().id(dto.getUser().getId()).build() : null)
                .group(dto.getGroup() != null ? Group.builder().id(dto.getGroup().getId()).build() : null)
                .build();
    }
}
