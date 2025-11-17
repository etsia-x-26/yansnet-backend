package com.etsia.comment.application.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CommentDto {
    private Integer id;
    private Integer postId;
    private Integer userId;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
