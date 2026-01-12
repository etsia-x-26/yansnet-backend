package com.etsia.message.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {
    private Integer id;
    private Integer conversationId;
    private Integer senderId;
    private String senderName;
    private String senderAvatar;
    private String content;
    private String url;
    private String type; // TEXT, IMAGE, FILE, AUDIO, VIDEO
    private Instant createdAt;
    private boolean read;
}
