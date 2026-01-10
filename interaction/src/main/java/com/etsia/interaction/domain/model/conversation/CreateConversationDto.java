package com.etsia.interaction.domain.model.conversation;


import com.etsia.common.domain.model.sub.ConversationRole;
import com.etsia.common.domain.model.sub.ConversationType;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import java.io.Serializable;
import java.util.Map;

@Getter
@Setter
@Value

public class CreateConversationDto implements Serializable {

    String title;
    String description;
    ConversationType type;
    // Permettre d'associer un role différent à chaque followerId
    Map<Integer, ConversationRole> followerRoles;

}
