package com.etsia.interaction.domain.model.conversation;

import com.etsia.common.domain.model.UserDto;
import com.etsia.common.domain.model.sub.ConversationRole;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import java.io.Serializable;

@Value
@Getter
@Setter
public class UserWithRoleDto implements Serializable {
    UserDto user;
    ConversationRole role;
}

