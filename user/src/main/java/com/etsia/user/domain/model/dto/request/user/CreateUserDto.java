package com.etsia.user.domain.model.dto.request.user;


import com.etsia.common.domain.model.BatchDto;
import com.etsia.common.domain.model.DepartmentDto;
import com.etsia.common.domain.model.UserCategoryDto;
import com.etsia.common.domain.model.sub.Email;
import com.etsia.common.domain.model.sub.PhoneNumber;

import com.etsia.common.infrastructure.config.EmailConverter;
import com.etsia.common.infrastructure.config.PhoneNumberConverter;
import com.etsia.common.infrastructure.entities.Batch;
import com.etsia.common.infrastructure.entities.Department;
import com.etsia.common.infrastructure.entities.UserCategory;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Value;
import org.hibernate.annotations.ColumnDefault;

import java.io.Serializable;



@Builder
@Value
public class CreateUserDto implements Serializable {

    Integer id;

    String email;

    String password;

    Boolean isActive;
    UserCategoryDto category;
    Boolean isBlocked;
    DepartmentDto department;
    BatchDto batch;
    PhoneNumber phoneNumber;
    int totalFollowers;
    int totalFollowing;
    int totalPosts;

}