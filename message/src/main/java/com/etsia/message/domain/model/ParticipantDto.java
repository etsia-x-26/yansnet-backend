package com.etsia.message.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantDto {
    private Integer userId;
    private String name;
    private String username;
    private String avatarUrl;
    private String role; // ADMIN, MEMBER
    private boolean online;
}
