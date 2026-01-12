package com.etsia.comment.application.dto;

import com.etsia.common.infrastructure.security.UserIdAware;
import lombok.Data;

/**
 * Request DTO for creating a new comment.
 * Implements UserIdAware to automatically receive the userId from the JWT token.
 */
@Data
public class CreateCommentRequest implements UserIdAware {
    private Integer postId;
    private Integer userId;
    private String content;
}
