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
//import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Value;
import org.hibernate.annotations.ColumnDefault;
//import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;



@Builder
@Value
public class CreateUserDto implements Serializable {

//    @NotBlank(message = "ID is required")
    Integer id;

//    @NotBlank(message = "Email is required")
    String email;

//    @NotBlank
    String password;

    Boolean isActive;

    Integer category_id;
    Boolean isBlocked;
    Integer department_id;
    Integer batch_id;
    String phoneNumber;
    String name;
    String username;
    String description;
    String urlprofile;
    int totalFollowers;
    int totalFollowing;
    int totalPosts;

}