package com.etsia.group.application.service;

import com.etsia.common.domain.model.GroupMemberDto;
import com.etsia.group.domain.model.dto.request.UpdateMemberRoleDto;
import com.etsia.group.domain.repository.GroupRepository;
import com.etsia.group.domain.service.GroupDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroupMemberUpdateRoleService {

    @Qualifier("gGroupRepository")
    private final GroupRepository groupRepository;

    @Qualifier("gGroupDomainService")
    private final GroupDomainService groupDomainService;

    public GroupMemberDto execute(UpdateMemberRoleDto updateMemberRoleDto) {
        if (!groupDomainService.isMemberOfGroup(updateMemberRoleDto.getGroupId(), updateMemberRoleDto.getUserId())) {
            throw new RuntimeException("User is not a member of this group");
        }
        return groupRepository.updateMemberRole(updateMemberRoleDto);
    }
}
