package com.etsia.user.domain.model.dto.request.user;

import lombok.Getter;
import lombok.Setter;
import lombok.Value;
import org.antlr.v4.runtime.misc.NotNull;

import java.io.Serializable;


@Getter
@Setter
@Value
public class DeleteUserDto implements Serializable {
    @NotNull()
    Integer id;
}