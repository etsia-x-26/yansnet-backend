package com.etsia.event.infrastructure.repository;

import com.etsia.common.infrastructure.entities.EventRSVP;
import com.etsia.common.infrastructure.entities.EventRSVPId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaEventRSVPRepository extends JpaRepository<EventRSVP, EventRSVPId> {
}
