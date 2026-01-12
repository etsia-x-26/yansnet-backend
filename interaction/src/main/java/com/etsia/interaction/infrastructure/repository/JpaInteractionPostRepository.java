package com.etsia.interaction.infrastructure.repository;

import com.etsia.common.infrastructure.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaInteractionPostRepository extends JpaRepository<Post, Integer> {
}
