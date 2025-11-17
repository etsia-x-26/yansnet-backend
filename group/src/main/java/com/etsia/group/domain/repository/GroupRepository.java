package com.etsia.group.domain.repository;

import com.etsia.common.domain.model.GroupDto;
import com.etsia.common.domain.model.GroupMemberDto;
import com.etsia.group.domain.model.dto.request.AddMemberDto;
import com.etsia.group.domain.model.dto.request.CreateGroupDto;
import com.etsia.group.domain.model.dto.request.UpdateGroupDto;
import com.etsia.group.domain.model.dto.request.UpdateMemberRoleDto;

import java.util.List;
import java.util.Optional;

public interface GroupRepository {
    GroupDto createGroup(CreateGroupDto createGroupDto);
    Optional<GroupDto> findById(Integer id);
    List<GroupDto> findAll();
    List<GroupDto> findByCreatedBy(Integer userId);
    GroupDto updateGroup(UpdateGroupDto updateGroupDto);
    void deleteGroup(Integer id);

    GroupMemberDto addMember(AddMemberDto addMemberDto);
    List<GroupMemberDto> findMembersByGroupId(Integer groupId);
    Optional<GroupMemberDto> findMember(Integer groupId, Integer userId);
    GroupMemberDto updateMemberRole(UpdateMemberRoleDto updateMemberRoleDto);
    void removeMember(Integer groupId, Integer userId);
    boolean isMemberOfGroup(Integer groupId, Integer userId);
}
