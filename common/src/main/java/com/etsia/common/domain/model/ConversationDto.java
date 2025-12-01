package com.etsia.common.domain.model;

import com.etsia.common.domain.model.sub.ConversationType;
import lombok.Builder;
import lombok.Value;

import java.io.Serializable;
import java.util.Set;

/**
 * DTO for {@link com.etsia.common.infrastructure.entities.Conversation}
 */
@Builder
@Value
public class ConversationDto implements Serializable {
    Integer id;
    String title;
    String description;
    ConversationType type;
}