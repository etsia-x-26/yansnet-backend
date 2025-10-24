package com.etsia.post.application.dto;

import com.etsia.common.domain.model.MediaDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreatePostRequest {
    private String content;
    private Integer userId;
    private List<MediaDto> medias;
}