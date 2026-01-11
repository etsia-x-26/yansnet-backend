package com.etsia.event.infrastructure.controller;

import com.etsia.common.infrastructure.entities.Event;
import com.etsia.event.domain.model.dto.request.CreateEventRequest;
import com.etsia.event.domain.model.dto.response.EventResponse;
import com.etsia.event.domain.service.EventService;
import com.etsia.event.infrastructure.mapper.EventMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
@Tag(name = "Event Management", description = "Endpoints for creating and managing university events")
public class EventController {

    private final EventService eventService;
    private final EventMapper eventMapper;

    @Operation(summary = "Create a new event", description = "Registers a new event in the system")
    @ApiResponse(responseCode = "201", description = "Event created successfully")
    @PostMapping
    public ResponseEntity<EventResponse> createEvent(@RequestBody CreateEventRequest request) {
        Event event = eventMapper.toEntity(request);
        Event savedEvent = eventService.createEvent(event);
        return new ResponseEntity<>(eventMapper.toResponse(savedEvent), HttpStatus.CREATED);
    }

    @Operation(summary = "Get event by ID", description = "Retrieves an event's details by its unique identifier")
    @ApiResponse(responseCode = "200", description = "Event found")
    @ApiResponse(responseCode = "404", description = "Event not found")
    @GetMapping("/{id}")
    public ResponseEntity<EventResponse> getEventById(@PathVariable Integer id) {
        return eventService.getEvent(id)
                .map(eventMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get all events", description = "Retrieves a paginated list of events")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of events")
    @GetMapping
    public ResponseEntity<Page<EventResponse>> getAllEvents(Pageable pageable) {
        return ResponseEntity.ok(eventService.getAllEvents(pageable).map(eventMapper::toResponse));
    }

    @Operation(summary = "Delete an event", description = "Removes an event and its related RSVPs")
    @ApiResponse(responseCode = "204", description = "Event deleted successfully")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Integer id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "RSVP to an event", description = "Registers a user's attendance status for an event")
    @ApiResponse(responseCode = "200", description = "RSVP recorded successfully")
    @PostMapping("/{id}/rsvp")
    public ResponseEntity<Void> rsvpEvent(@PathVariable Integer id, @RequestParam Integer userId, @RequestParam String status) {
        eventService.rsvpEvent(id, userId, status);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Cancel RSVP", description = "Removes a user's RSVP status for an event")
    @ApiResponse(responseCode = "204", description = "RSVP cancelled successfully")
    @DeleteMapping("/{id}/rsvp")
    public ResponseEntity<Void> cancelRsvp(@PathVariable Integer id, @RequestParam Integer userId) {
        eventService.cancelRsvp(id, userId);
        return ResponseEntity.noContent().build();
    }
}
