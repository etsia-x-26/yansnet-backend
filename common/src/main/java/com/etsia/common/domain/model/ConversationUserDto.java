package com.etsia.common.domain.model;

import com.etsia.common.domain.model.sub.ConversationRole;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.etsia.common.infrastructure.entities.ConversationUser}
 */
@Value
public class ConversationUserDto implements Serializable {
    Integer id;
    @NotNull
    ConversationDto conversation;
    UserDto user;
    ConversationRole role;

}