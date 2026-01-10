package com.etsia.event.infrastructure.repository;

import com.etsia.common.infrastructure.entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaEventRepository extends JpaRepository<Event, Integer> {
}
