package com.etsia.interaction.domain.model.conversation;

import com.etsia.common.domain.model.sub.ConversationType;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serializable;


@Getter
@Setter
@Value

public class UpdateConversationDto implements Serializable {

    @NotBlank(message = "ID is required")
    Integer id;
    String title;
    String description;
    ConversationType type;
}
