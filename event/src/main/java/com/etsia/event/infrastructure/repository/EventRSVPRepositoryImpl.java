package com.etsia.event.infrastructure.repository;

import com.etsia.common.infrastructure.entities.EventRSVP;
import com.etsia.common.infrastructure.entities.EventRSVPId;
import com.etsia.event.domain.repository.EventRSVPRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class EventRSVPRepositoryImpl implements EventRSVPRepository {

    private final JpaEventRSVPRepository jpaEventRSVPRepository;

    @Override
    public EventRSVP save(EventRSVP rsvp) {
        return jpaEventRSVPRepository.save(rsvp);
    }

    @Override
    public Optional<EventRSVP> findById(EventRSVPId id) {
        return jpaEventRSVPRepository.findById(id);
    }

    @Override
    public void deleteById(EventRSVPId id) {
        jpaEventRSVPRepository.deleteById(id);
    }
}
