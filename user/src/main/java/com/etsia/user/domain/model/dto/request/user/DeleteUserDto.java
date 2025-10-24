package com.etsia.user.domain.model.dto.request.user;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import java.io.Serializable;


@Getter
@Setter
@Value
public class DeleteUserDto implements Serializable {
    @NotNull(message = "Id is required")
    Integer id;
}