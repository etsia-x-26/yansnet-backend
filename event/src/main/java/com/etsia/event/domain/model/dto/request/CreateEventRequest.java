package com.etsia.event.domain.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateEventRequest {
    private String title;
    private String description;
    private String category;
    private Instant eventDate;
    private String location;
    private Integer organizerId;
    private Integer maxParticipants;
    private String imageUrl;
}
