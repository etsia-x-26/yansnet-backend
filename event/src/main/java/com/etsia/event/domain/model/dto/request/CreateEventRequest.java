package com.etsia.event.domain.model.dto.request;

import com.etsia.common.infrastructure.security.UserIdAware;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Request DTO for creating a new event.
 * Implements UserIdAware to automatically receive the organizerId from the JWT token.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateEventRequest implements UserIdAware {
    private String title;
    private String description;
    private String category;
    private Instant eventDate;
    private String location;
    private Integer organizerId;
    private Integer maxParticipants;
    private String imageUrl;

    @Override
    public void setUserId(Integer userId) {
        this.organizerId = userId;
    }

    @Override
    public Integer getUserId() {
        return this.organizerId;
    }
}
