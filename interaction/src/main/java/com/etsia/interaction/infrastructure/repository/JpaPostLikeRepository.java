package com.etsia.interaction.infrastructure.repository;

import com.etsia.common.infrastructure.entities.PostLike;
import com.etsia.common.infrastructure.entities.PostLikeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaPostLikeRepository extends JpaRepository<PostLike, PostLikeId> {
}
