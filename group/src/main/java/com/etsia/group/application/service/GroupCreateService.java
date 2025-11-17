package com.etsia.group.application.service;

import com.etsia.common.domain.model.GroupDto;
import com.etsia.group.domain.model.dto.request.CreateGroupDto;
import com.etsia.group.domain.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroupCreateService {

    @Qualifier("gGroupRepository")
    private final GroupRepository groupRepository;

    public GroupDto execute(CreateGroupDto createGroupDto) {
        if (createGroupDto.getName() == null || createGroupDto.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Group name is required");
        }
        return groupRepository.createGroup(createGroupDto);
    }
}
