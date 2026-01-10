package com.etsia.group.application.service;

import com.etsia.common.domain.model.GroupMemberDto;
import com.etsia.group.domain.service.GroupDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupMemberFindService {

    @Qualifier("gGroupDomainService")
    private final GroupDomainService groupDomainService;

    public List<GroupMemberDto> execute(Integer groupId) {
        if (!groupDomainService.groupExists(groupId)) {
            throw new RuntimeException("Group not found with id: " + groupId);
        }
        return groupDomainService.findMembersByGroupId(groupId);
    }
}
