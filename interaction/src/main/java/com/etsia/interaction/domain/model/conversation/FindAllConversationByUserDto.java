package com.etsia.interaction.domain.model.conversation;

import com.etsia.common.domain.model.ConversationDto;
import com.etsia.common.domain.model.UserDto;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Value

public class FindAllConversationByUserDto implements Serializable {

    UserDto User;
    List<ConversationDto> Conversations;

}
