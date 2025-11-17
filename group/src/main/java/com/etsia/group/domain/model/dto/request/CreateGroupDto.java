package com.etsia.group.domain.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateGroupDto {
    private String name;
    private String description;
    private String profileImageUrl;
    private Integer createdBy;
}
