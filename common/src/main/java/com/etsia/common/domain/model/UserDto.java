package com.etsia.common.domain.model;

import com.etsia.common.domain.model.sub.Email;
import com.etsia.common.domain.model.sub.PhoneNumber;
import lombok.Builder;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.etsia.common.infrastructure.entities.User}
 */


@Builder
@Value
public class UserDto implements Serializable {
    Integer id;
    String password;
    Boolean isActive;
    UserCategoryDto category;
    Boolean isBlocked;
    DepartmentDto department;
    BatchDto batch;
    Email email;
    PhoneNumber phoneNumber;
    String name;
    String username;
    String description;
    String urlprofile;
    int totalFollowers;
    int totalFollowing;
    int totalPosts;
}