package com.etsia.interaction.domain.model.conversation;


import com.etsia.common.domain.model.sub.ConversationType;
import lombok.Builder;
import lombok.Value;

import java.io.Serializable;

@Builder
@Value
public class CreateConversationDto implements Serializable {
    String title;
    String description;
    ConversationType type;

}
