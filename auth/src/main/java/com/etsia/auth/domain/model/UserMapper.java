package com.etsia.auth.domain.model;

import com.etsia.common.infrastructure.entities.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public AuthUser toDomainModel(User userEntity) {
        if (userEntity == null) {
            return null;
        }

        Integer categoryId = userEntity.getCategory() != null ? userEntity.getCategory().getId() : null;
        Integer departmentId = userEntity.getDepartment() != null ? userEntity.getDepartment().getId() : null;
        Integer batchId = userEntity.getBatch() != null ? userEntity.getBatch().getId() : null;

        return new AuthUser(
                userEntity.getId(),
                userEntity.getEmail(),
                userEntity.getPhoneNumber(),
                userEntity.getPassword(),
                userEntity.getIsActive() != null ? userEntity.getIsActive() : true,
                userEntity.getIsBlocked() != null ? userEntity.getIsBlocked() : false,
                userEntity.getTotalFollowers(),
                userEntity.getTotalFollowing(),
                userEntity.getTotalPosts(),
                categoryId,
                departmentId,
                batchId
        );
    }

    public User toEntity(AuthUser authUser) {
        if (authUser == null) {
            return null;
        }

        User.UserBuilder builder = User.builder()
                .email(authUser.getEmail())
                .phoneNumber(authUser.getPhoneNumber())
                .password(authUser.getPassword())
                .isActive(authUser.isActive())
                .isBlocked(authUser.isBlocked())
                .totalFollowers(authUser.getTotalFollowers())
                .totalFollowing(authUser.getTotalFollowing())
                .totalPosts(authUser.getTotalPosts());

        // Only set ID if it's not null (for updates)
        // For new users (null ID), let Hibernate generate it
        if (authUser.getUserId() != null) {
            builder.id(authUser.getUserId());
        }

        return builder.build();
    }

    public void updateEntity(User userEntity, AuthUser authUser) {
        if (userEntity == null || authUser == null) {
            return;
        }

        userEntity.setEmail(authUser.getEmail());
        userEntity.setPhoneNumber(authUser.getPhoneNumber());
        userEntity.setPassword(authUser.getPassword());
        userEntity.setIsActive(authUser.isActive());
        userEntity.setIsBlocked(authUser.isBlocked());
        userEntity.setTotalFollowers(authUser.getTotalFollowers());
        userEntity.setTotalFollowing(authUser.getTotalFollowing());
        userEntity.setTotalPosts(authUser.getTotalPosts());
    }
}