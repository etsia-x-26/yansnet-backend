package com.etsia.group.application.service;

import com.etsia.common.domain.model.GroupDto;
import com.etsia.group.domain.model.dto.request.UpdateGroupDto;
import com.etsia.group.domain.repository.GroupRepository;
import com.etsia.group.domain.service.GroupDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroupUpdateService {

    @Qualifier("gGroupRepository")
    private final GroupRepository groupRepository;

    @Qualifier("gGroupDomainService")
    private final GroupDomainService groupDomainService;

    public GroupDto execute(UpdateGroupDto updateGroupDto) {
        if (!groupDomainService.groupExists(updateGroupDto.getId())) {
            throw new RuntimeException("Group not found with id: " + updateGroupDto.getId());
        }
        return groupRepository.updateGroup(updateGroupDto);
    }
}
