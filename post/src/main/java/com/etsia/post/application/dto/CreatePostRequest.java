package com.etsia.post.application.dto;

import com.etsia.common.domain.model.MediaDto;
import com.etsia.common.infrastructure.security.UserIdAware;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Request DTO for creating a new post.
 * Implements UserIdAware to automatically receive the userId from the JWT token.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreatePostRequest implements UserIdAware {
    private String content;
    private Integer userId;
    private List<MediaDto> medias;
}