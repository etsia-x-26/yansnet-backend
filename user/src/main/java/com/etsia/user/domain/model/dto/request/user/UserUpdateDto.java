package com.etsia.user.domain.model.dto.request.user;

import com.etsia.common.domain.model.BatchDto;
import com.etsia.common.domain.model.DepartmentDto;
import com.etsia.common.domain.model.UserCategoryDto;
import com.etsia.common.domain.model.sub.PhoneNumber;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;
//import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;


@Value
@Getter
@Setter
public class UserUpdateDto implements Serializable {
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
    String bio;
    String profilePictureUrl;
    Boolean isMentor;
    Integer promotionYear;
    Integer totalFollowers;
    Integer totalFollowing;
    Integer totalPosts;
}