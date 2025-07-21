package com.etsia.user.infrastructure.config;

import com.etsia.common.domain.model.UserDto;
import com.etsia.common.infrastructure.config.Mapper;
import com.etsia.common.infrastructure.entities.User;

public class MapperUser {

    public static UserDto mapToUserDto(User user) {
        return Mapper.toUserDto(user);
    }

}
