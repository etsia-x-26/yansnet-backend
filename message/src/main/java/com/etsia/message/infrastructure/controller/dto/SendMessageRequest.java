package com.etsia.message.infrastructure.controller.dto;

import com.etsia.common.infrastructure.security.UserIdAware;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Request DTO for sending a message.
 * Implements UserIdAware to automatically receive the userId from the JWT token.
 */
@Data
public class SendMessageRequest implements UserIdAware {
    @NotNull
    private Integer conversationId;
    @NotBlank
    private String content;
    private String type; // TEXT, IMAGE, FILE, AUDIO, VIDEO
    private String url;
    private Integer userId;
}
