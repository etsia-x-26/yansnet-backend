package com.etsia.group.application.service;

import com.etsia.group.domain.repository.GroupRepository;
import com.etsia.group.domain.service.GroupDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroupDeleteService {

    @Qualifier("gGroupRepository")
    private final GroupRepository groupRepository;

    @Qualifier("gGroupDomainService")
    private final GroupDomainService groupDomainService;

    public void execute(Integer groupId) {
        if (!groupDomainService.groupExists(groupId)) {
            throw new RuntimeException("Group not found with id: " + groupId);
        }
        groupRepository.deleteGroup(groupId);
    }
}
