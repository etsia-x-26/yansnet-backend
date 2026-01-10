package com.etsia.event.infrastructure.controller;

import com.etsia.common.infrastructure.entities.Event;
import com.etsia.event.domain.model.dto.request.CreateEventRequest;
import com.etsia.event.domain.model.dto.response.EventResponse;
import com.etsia.event.domain.service.EventService;
import com.etsia.event.infrastructure.mapper.EventMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final EventMapper eventMapper;

    @PostMapping
    public ResponseEntity<EventResponse> createEvent(@RequestBody CreateEventRequest request) {
        Event event = eventMapper.toEntity(request);
        Event savedEvent = eventService.createEvent(event);
        return new ResponseEntity<>(eventMapper.toResponse(savedEvent), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventResponse> getEventById(@PathVariable Integer id) {
        return eventService.getEvent(id)
                .map(eventMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<Page<EventResponse>> getAllEvents(Pageable pageable) {
        return ResponseEntity.ok(eventService.getAllEvents(pageable).map(eventMapper::toResponse));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Integer id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/rsvp")
    public ResponseEntity<Void> rsvpEvent(@PathVariable Integer id, @RequestParam Integer userId, @RequestParam String status) {
        eventService.rsvpEvent(id, userId, status);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/rsvp")
    public ResponseEntity<Void> cancelRsvp(@PathVariable Integer id, @RequestParam Integer userId) {
        eventService.cancelRsvp(id, userId);
        return ResponseEntity.noContent().build();
    }
}
