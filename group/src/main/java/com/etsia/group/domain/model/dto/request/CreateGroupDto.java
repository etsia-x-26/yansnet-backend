package com.etsia.group.domain.model.dto.request;

import com.etsia.common.infrastructure.security.UserIdAware;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for creating a new group.
 * Implements UserIdAware to automatically receive the createdBy from the JWT token.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateGroupDto implements UserIdAware {
    private String name;
    private String description;
    private String profileImageUrl;
    private Integer createdBy;

    @Override
    public void setUserId(Integer userId) {
        this.createdBy = userId;
    }

    @Override
    public Integer getUserId() {
        return this.createdBy;
    }
}
