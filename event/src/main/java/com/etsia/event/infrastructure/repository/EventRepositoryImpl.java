package com.etsia.event.infrastructure.repository;

import com.etsia.common.infrastructure.entities.Event;
import com.etsia.event.domain.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class EventRepositoryImpl implements EventRepository {

    private final JpaEventRepository jpaEventRepository;

    @Override
    public Event save(Event event) {
        return jpaEventRepository.save(event);
    }

    @Override
    public Optional<Event> findById(Integer id) {
        return jpaEventRepository.findById(id);
    }

    @Override
    public void deleteById(Integer id) {
        jpaEventRepository.deleteById(id);
    }

    @Override
    public Page<Event> findAll(Pageable pageable) {
        return jpaEventRepository.findAll(pageable);
    }
}
