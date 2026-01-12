package com.etsia.interaction.infrastructure.repository;

import com.etsia.common.infrastructure.entities.CommentDislike;
import com.etsia.common.infrastructure.entities.CommentDislikeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaCommentDislikeRepository extends JpaRepository<CommentDislike, CommentDislikeId> {
}
