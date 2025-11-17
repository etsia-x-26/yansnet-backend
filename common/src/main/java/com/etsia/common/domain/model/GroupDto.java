package com.etsia.common.domain.model;

import lombok.Builder;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.etsia.common.infrastructure.entities.Group}
 */
@Builder
@Value
public class GroupDto implements Serializable {
    Integer id;
    String name;
    String description;
    String profileImageUrl;
    Integer createdBy;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
