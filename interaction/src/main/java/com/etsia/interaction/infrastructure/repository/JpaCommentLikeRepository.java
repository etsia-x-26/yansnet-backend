package com.etsia.interaction.infrastructure.repository;

import com.etsia.common.infrastructure.entities.CommentLike;
import com.etsia.common.infrastructure.entities.CommentLikeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaCommentLikeRepository extends JpaRepository<CommentLike, CommentLikeId> {
}
