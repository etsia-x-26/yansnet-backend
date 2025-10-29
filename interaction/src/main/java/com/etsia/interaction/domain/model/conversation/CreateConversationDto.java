package com.etsia.interaction.domain.model.conversation;


import com.etsia.common.domain.model.sub.ConversationType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import java.io.Serializable;

@Getter
@Setter
@Value

public class CreateConversationDto implements Serializable {
    Integer id;
    String title;
    String description;
    ConversationType type;

}
