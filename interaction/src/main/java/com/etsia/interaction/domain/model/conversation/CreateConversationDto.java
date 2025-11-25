package com.etsia.interaction.domain.model.conversation;


import com.etsia.common.domain.model.sub.ConversationType;
import com.etsia.common.domain.model.sub.ConversationTypeRenew;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import java.io.Serializable;

@Getter
@Setter
@Value

public class CreateConversationDto implements Serializable {

    String title;
    String description;
    ConversationType type;

}
