package com.etsia.interaction.infrastructure.repository;

import com.etsia.common.infrastructure.entities.PostDislike;
import com.etsia.common.infrastructure.entities.PostDislikeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaPostDislikeRepository extends JpaRepository<PostDislike, PostDislikeId> {
}
