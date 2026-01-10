package com.etsia.message.infrastructure.controller.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {
    private String content;
    private String sender;
    private MessageType type;
    private String timestamp;

    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE,
        TYPING
    }
}
