package com.etsia.common.domain.model;

import com.etsia.common.infrastructure.entities.Media;
import lombok.Builder;
import lombok.Value;

import java.io.Serializable;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * DTO for {@link com.etsia.common.infrastructure.entities.Post}
 */


@Builder
@Value
public class PostDto implements Serializable {
    Integer id;
    String content;
    Instant deletedAt;
    Instant createdAt;
    UserDto user;
    ChannelDto channel;
    List<MediaDto> media;
    int totalLikes;
    int totalComments;

}