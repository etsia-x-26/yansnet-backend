package com.etsia.event.domain.repository;

import com.etsia.common.infrastructure.entities.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface EventRepository {
    Event save(Event event);
    Optional<Event> findById(Integer id);
    void deleteById(Integer id);
    Page<Event> findAll(Pageable pageable);
}
