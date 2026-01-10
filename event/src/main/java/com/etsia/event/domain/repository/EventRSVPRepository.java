package com.etsia.event.domain.repository;

import com.etsia.common.infrastructure.entities.EventRSVP;
import com.etsia.common.infrastructure.entities.EventRSVPId;

import java.util.Optional;

public interface EventRSVPRepository {
    EventRSVP save(EventRSVP rsvp);
    Optional<EventRSVP> findById(EventRSVPId id);
    void deleteById(EventRSVPId id);
}
