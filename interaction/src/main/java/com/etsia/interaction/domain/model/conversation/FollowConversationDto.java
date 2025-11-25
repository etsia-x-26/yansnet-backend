package com.etsia.interaction.domain.model.conversation;

import com.etsia.common.domain.model.sub.ConversationRole;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import java.io.Serializable;

@Getter
@Setter
@Value

public class FollowConversationDto implements Serializable {

    Integer[] followerIds;
    ConversationRole role;
}
