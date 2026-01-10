package com.etsia.interaction.domain.model.conversation;

import com.etsia.common.domain.model.ConversationDto;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import java.io.Serializable;
import java.util.List;


@Value
@Getter
@Setter
public class FindAllUserByConversationDto implements Serializable {
    Integer id;
    ConversationDto conversation;
    List<UserWithRoleDto> users; // Chaque utilisateur avec son rôle spécifique
}
