package com.etsia.group.domain.model.dto.request;

import com.etsia.common.domain.model.sub.GroupRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddMemberDto {
    private Integer groupId;
    private Integer userId;
    private GroupRole role;
}
