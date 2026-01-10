package com.etsia.event.domain.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventResponse {
    private Integer id;
    private String title;
    private String description;
    private String category;
    private Instant eventDate;
    private String location;
    private Integer organizerId;
    private String organizerName; // Useful for UI
    private Integer maxParticipants;
    private String imageUrl;
    private Instant createdAt;
}
