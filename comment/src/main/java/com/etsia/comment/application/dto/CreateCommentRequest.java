package com.etsia.comment.application.dto;

import lombok.Data;

@Data
public class CreateCommentRequest {
    private Integer postId;
    private Integer userId;
    private String content;
}
