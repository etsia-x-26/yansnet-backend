package com.etsia.interaction.domain.model.conversation;

import com.etsia.common.domain.model.sub.ConversationType;
import lombok.Builder;
import lombok.Value;

import java.io.Serializable;

@Builder
@Value
public class UpdateConversationDto implements Serializable {
    Integer id;
    String title;
    String description;
    ConversationType type;
}
