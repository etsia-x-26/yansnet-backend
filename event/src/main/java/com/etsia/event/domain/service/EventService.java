package com.etsia.event.domain.service;

import com.etsia.common.infrastructure.entities.Event;
import com.etsia.common.infrastructure.entities.User;
import com.etsia.event.domain.model.dto.response.EventResponse;
import com.etsia.event.domain.repository.EventRSVPRepository;
import com.etsia.event.domain.repository.EventRepository;
import com.etsia.event.infrastructure.mapper.EventMapper;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final EventRSVPRepository rsvpRepository;
    private final EventMapper eventMapper;

    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    public Optional<EventResponse> getEvent(Integer id) {
        return eventRepository.findById(id).map(eventMapper::toResponse);
    }

    public Page<EventResponse> getAllEvents(Pageable pageable) {
        return eventRepository.findAll(pageable).map(eventMapper::toResponse);
    }

    public void deleteEvent(Integer id) {
        eventRepository.deleteById(id);
    }

    public com.etsia.common.infrastructure.entities.EventRSVP rsvpEvent(Integer eventId, Integer userId, String status) {
        com.etsia.common.infrastructure.entities.EventRSVPId rsvpId = com.etsia.common.infrastructure.entities.EventRSVPId.builder()
                .eventId(eventId)
                .userId(userId)
                .build();

        com.etsia.common.infrastructure.entities.EventRSVP rsvp = com.etsia.common.infrastructure.entities.EventRSVP.builder()
                .id(rsvpId)
                .event(com.etsia.common.infrastructure.entities.Event.builder().id(eventId).build())
                .user(com.etsia.common.infrastructure.entities.User.builder().id(userId).build())
                .status(status)
                .createdAt(java.time.Instant.now())
                .build();

        return rsvpRepository.save(rsvp);
    }

    public void cancelRsvp(Integer eventId, Integer userId) {
        com.etsia.common.infrastructure.entities.EventRSVPId rsvpId = com.etsia.common.infrastructure.entities.EventRSVPId.builder()
                .eventId(eventId)
                .userId(userId)
                .build();
        rsvpRepository.deleteById(rsvpId);
    }
}
