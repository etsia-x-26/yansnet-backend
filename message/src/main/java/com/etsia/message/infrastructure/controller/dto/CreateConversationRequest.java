package com.etsia.message.infrastructure.controller.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class CreateConversationRequest {
    private String title;
    private String description;
    @NotNull
    private String type; // DIRECT or GROUP
    @NotNull
    private List<Integer> participantIds;
}
