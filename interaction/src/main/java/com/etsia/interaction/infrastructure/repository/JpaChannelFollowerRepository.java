package com.etsia.interaction.infrastructure.repository;

import com.etsia.common.infrastructure.entities.ChannelFollower;
import com.etsia.common.infrastructure.entities.ChannelFollowerId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

@Service
public interface JpaChannelFollowerRepository extends JpaRepository<ChannelFollower, ChannelFollowerId>, JpaSpecificationExecutor<ChannelFollower> {

  @Query("select exists(select 1 from ChannelFollower where user.id = :userId and channel.id = :channelId)")
  boolean existsByUserIdAndChannelId(Integer userId, Integer channelId);

  long countByUserId(Integer userId);
}