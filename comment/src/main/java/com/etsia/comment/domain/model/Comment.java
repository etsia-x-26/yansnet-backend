package com.etsia.comment.domain.model;

import com.etsia.comment.application.dto.CommentDto;
import com.etsia.comment.application.dto.CreateCommentRequest;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer postId;
    private Integer userId;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public static Comment fromRequest(CreateCommentRequest req) {
        Comment c = new Comment();
        c.setPostId(req.getPostId());
        c.setUserId(req.getUserId());
        c.setContent(req.getContent());
        return c;
    }

    public CommentDto toDto() {
        CommentDto dto = new CommentDto();
        dto.setId(id);
        dto.setPostId(postId);
        dto.setUserId(userId);
        dto.setContent(content);
        dto.setCreatedAt(createdAt);
        dto.setUpdatedAt(updatedAt);
        return dto;
    }
}
