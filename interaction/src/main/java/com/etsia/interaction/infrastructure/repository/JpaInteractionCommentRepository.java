package com.etsia.interaction.infrastructure.repository;

import com.etsia.common.infrastructure.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaInteractionCommentRepository extends JpaRepository<Comment, Integer> {
}
