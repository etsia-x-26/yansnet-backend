package com.etsia.message.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConversationDto {
    private Integer id;
    private String title;
    private String description;
    private String type; // DIRECT, GROUP
    private List<ParticipantDto> participants;
    private MessageDto lastMessage;
    private int unreadCount;
    private Instant createdAt;
    private Instant updatedAt;
}
