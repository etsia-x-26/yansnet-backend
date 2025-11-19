package com.etsia.group.domain.service;

import com.etsia.common.domain.model.GroupDto;
import com.etsia.common.domain.model.GroupMemberDto;
import com.etsia.group.domain.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service(value = "gGroupDomainService")
@RequiredArgsConstructor
public class GroupDomainService {

    @Qualifier("gGroupRepository")
    private final GroupRepository groupRepository;

    public Optional<GroupDto> findById(Integer id) {
        return groupRepository.findById(id);
    }

    public List<GroupDto> findAll() {
        return groupRepository.findAll();
    }

    public List<GroupDto> findByCreatedBy(Integer userId) {
        return groupRepository.findByCreatedBy(userId);
    }

    public List<GroupMemberDto> findMembersByGroupId(Integer groupId) {
        return groupRepository.findMembersByGroupId(groupId);
    }

    public boolean isMemberOfGroup(Integer groupId, Integer userId) {
        return groupRepository.isMemberOfGroup(groupId, userId);
    }

    public boolean groupExists(Integer groupId) {
        return groupRepository.findById(groupId).isPresent();
    }

    public Optional<GroupMemberDto> findMember(Integer groupId, Integer userId) {
        return groupRepository.findMember(groupId, userId);
    }
}
