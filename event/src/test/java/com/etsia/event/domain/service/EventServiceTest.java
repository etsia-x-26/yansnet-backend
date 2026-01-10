package com.etsia.event.domain.service;

import com.etsia.common.infrastructure.entities.Event;
import com.etsia.common.infrastructure.entities.EventRSVP;
import com.etsia.common.infrastructure.entities.User;
import com.etsia.event.domain.repository.EventRSVPRepository;
import com.etsia.event.domain.repository.EventRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private EventRSVPRepository eventRSVPRepository;

    @InjectMocks
    private EventService eventService;

    @Test
    void createEvent_ShouldReturnSavedEvent() {
        Event event = Event.builder()
                .title("Tech Talk")
                .eventDate(Instant.now())
                .organizer(User.builder().id(1).build())
                .build();

        when(eventRepository.save(any(Event.class))).thenReturn(event);

        Event saved = eventService.createEvent(event);

        assertNotNull(saved);
        assertEquals("Tech Talk", saved.getTitle());
        verify(eventRepository, times(1)).save(event);
    }

    @Test
    void getEvent_ShouldReturnEvent_WhenExists() {
        Event event = Event.builder().id(1).title("Tech Talk").build();
        when(eventRepository.findById(1)).thenReturn(Optional.of(event));

        Optional<Event> found = eventService.getEvent(1);

        assertTrue(found.isPresent());
        assertEquals("Tech Talk", found.get().getTitle());
    }

    @Test
    void getAllEvents_ShouldReturnPage() {
        Event event = Event.builder().id(1).title("Tech Talk").build();
        Page<Event> page = new PageImpl<>(List.of(event));
        when(eventRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<Event> result = eventService.getAllEvents(Pageable.unpaged());

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void rsvpEvent_ShouldSaveRSVP() {
        EventRSVP rsvp = EventRSVP.builder().status("GOING").build();
        when(eventRSVPRepository.save(any(EventRSVP.class))).thenReturn(rsvp);

        EventRSVP saved = eventService.rsvpEvent(1, 10, "GOING");

        assertNotNull(saved);
        assertEquals("GOING", saved.getStatus());
        verify(eventRSVPRepository, times(1)).save(any(EventRSVP.class));
    }
}
