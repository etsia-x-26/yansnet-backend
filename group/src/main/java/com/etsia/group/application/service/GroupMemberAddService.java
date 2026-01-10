package com.etsia.group.application.service;

import com.etsia.common.domain.model.GroupMemberDto;
import com.etsia.group.domain.model.dto.request.AddMemberDto;
import com.etsia.group.domain.repository.GroupRepository;
import com.etsia.group.domain.service.GroupDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroupMemberAddService {

    @Qualifier("gGroupRepository")
    private final GroupRepository groupRepository;

    @Qualifier("gGroupDomainService")
    private final GroupDomainService groupDomainService;

    public GroupMemberDto execute(AddMemberDto addMemberDto) {
        if (!groupDomainService.groupExists(addMemberDto.getGroupId())) {
            throw new RuntimeException("Group not found with id: " + addMemberDto.getGroupId());
        }
        if (groupDomainService.isMemberOfGroup(addMemberDto.getGroupId(), addMemberDto.getUserId())) {
            throw new RuntimeException("User is already a member of this group");
        }
        return groupRepository.addMember(addMemberDto);
    }
}
