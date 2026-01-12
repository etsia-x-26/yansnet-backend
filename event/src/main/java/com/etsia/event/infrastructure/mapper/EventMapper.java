package com.etsia.event.infrastructure.mapper;

import com.etsia.common.infrastructure.entities.Event;
import com.etsia.common.infrastructure.entities.User;
import com.etsia.event.domain.model.dto.request.CreateEventRequest;
import com.etsia.event.domain.model.dto.response.EventResponse;
import org.springframework.stereotype.Component;

@Component
public class EventMapper {

    public Event toEntity(CreateEventRequest request) {
        return Event.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .category(request.getCategory())
                .eventDate(request.getEventDate())
                .location(request.getLocation())
                .organizer(User.builder().id(request.getOrganizerId()).build()) // Proxy
                .maxParticipants(request.getMaxParticipants())
                .imageUrl(request.getImageUrl())
                .createdAt(java.time.Instant.now())
                .build();
    }

    public EventResponse toResponse(Event event) {
        return EventResponse.builder()
                .id(event.getId())
                .title(event.getTitle())
                .description(event.getDescription())
                .category(event.getCategory())
                .eventDate(event.getEventDate())
                .location(event.getLocation())
                .organizerId(event.getOrganizer() != null ? event.getOrganizer().getId() : null)
                .organizerName(event.getOrganizer() != null ? event.getOrganizer().getName() : null)
                .maxParticipants(event.getMaxParticipants())
                .imageUrl(event.getImageUrl())
                .createdAt(event.getCreatedAt())
                .build();
    }
}
