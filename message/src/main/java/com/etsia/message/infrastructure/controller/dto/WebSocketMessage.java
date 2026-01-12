package com.etsia.message.infrastructure.controller.dto;

import com.etsia.message.domain.model.MessageDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WebSocketMessage {
    private String action; // MESSAGE, TYPING, READ, USER_JOINED, USER_LEFT
    private Integer conversationId;
    private Integer senderId;
    private String senderName;
    private MessageDto message;
    private Object payload;
}
