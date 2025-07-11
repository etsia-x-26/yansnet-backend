package com.etsia.interaction.infrastructure.repository;

import com.etsia.common.infrastructure.entities.Channel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChannelRepository extends JpaRepository<Channel, Integer> {
    
}