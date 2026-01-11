package com.etsia.interaction.infrastructure.repository;

import com.etsia.common.infrastructure.entities.User;
import com.etsia.common.infrastructure.entities.UserFollow;
import com.etsia.common.infrastructure.entities.UserFollowId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaUserFollowRepository extends JpaRepository<UserFollow, UserFollowId>, JpaSpecificationExecutor<UserFollow> {

  @Query("select exists(select 1 from UserFollow where follower.id = :followerId and followed.id = :followedId)")
  boolean existsByFollowerIdAndFollowedId(Integer followerId, Integer followedId);

  long countByFollowerId(Integer followerId);

  @Query("SELECT u FROM User u WHERE u.id != :userId AND u.id NOT IN (SELECT f.followed.id FROM UserFollow f WHERE f.follower.id = :userId) ORDER BY u.totalFollowers DESC")
  List<User> findSuggestions(Integer userId, Pageable pageable);

  @Query("SELECT COUNT(f2.followed.id) FROM UserFollow f1 JOIN UserFollow f2 ON f1.followed.id = f2.follower.id WHERE f1.follower.id = :userId AND f2.followed.id = :targetUserId")
  long countMutualFollows(Integer userId, Integer targetUserId);
}