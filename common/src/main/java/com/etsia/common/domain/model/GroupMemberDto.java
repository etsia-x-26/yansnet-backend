package com.etsia.common.domain.model;

import com.etsia.common.domain.model.sub.GroupRole;
import lombok.Builder;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.etsia.common.infrastructure.entities.GroupMember}
 */
@Builder
@Value
public class GroupMemberDto implements Serializable {
    Integer userId;
    Integer groupId;
    GroupRole role;
    LocalDateTime joinedAt;
    UserDto user;
    GroupDto group;
}
