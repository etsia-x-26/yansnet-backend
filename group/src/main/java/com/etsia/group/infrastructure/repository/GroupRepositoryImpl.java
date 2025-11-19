package com.etsia.group.infrastructure.repository;

import com.etsia.common.domain.model.GroupDto;
import com.etsia.common.domain.model.GroupMemberDto;
import com.etsia.common.domain.model.sub.GroupRole;
import com.etsia.common.infrastructure.config.Mapper;
import com.etsia.common.infrastructure.entities.Group;
import com.etsia.common.infrastructure.entities.GroupMember;
import com.etsia.common.infrastructure.entities.GroupMemberId;
import com.etsia.common.infrastructure.entities.User;
import com.etsia.group.domain.model.dto.request.AddMemberDto;
import com.etsia.group.domain.model.dto.request.CreateGroupDto;
import com.etsia.group.domain.model.dto.request.UpdateGroupDto;
import com.etsia.group.domain.model.dto.request.UpdateMemberRoleDto;
import com.etsia.group.domain.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository(value = "gGroupRepository")
@RequiredArgsConstructor
public class GroupRepositoryImpl implements GroupRepository {

    @Qualifier("gJGroupRepository")
    private final JpaGroupRepository jpaGroupRepository;

    @Qualifier("gJGroupMemberRepository")
    private final JpaGroupMemberRepository jpaGroupMemberRepository;

    @Override
    public GroupDto createGroup(CreateGroupDto createGroupDto) {
        Group group = new Group();
        group.setName(createGroupDto.getName());
        group.setDescription(createGroupDto.getDescription());
        group.setProfileImageUrl(createGroupDto.getProfileImageUrl());

        if (createGroupDto.getCreatedBy() != null) {
            User creator = new User();
            creator.setId(createGroupDto.getCreatedBy());
            group.setCreatedBy(creator);
        }

        Group savedGroup = jpaGroupRepository.save(group);
        return Mapper.toGroupDto(savedGroup);
    }

    @Override
    public Optional<GroupDto> findById(Integer id) {
        return jpaGroupRepository.findById(id)
                .map(Mapper::toGroupDto);
    }

    @Override
    public List<GroupDto> findAll() {
        return Mapper.toGroupDtos(jpaGroupRepository.findAll());
    }

    @Override
    public List<GroupDto> findByCreatedBy(Integer userId) {
        return Mapper.toGroupDtos(jpaGroupRepository.findByCreatedBy_Id(userId));
    }

    @Override
    public GroupDto updateGroup(UpdateGroupDto updateGroupDto) {
        Group group = jpaGroupRepository.findById(updateGroupDto.getId())
                .orElseThrow(() -> new RuntimeException("Group not found"));

        if (updateGroupDto.getName() != null) {
            group.setName(updateGroupDto.getName());
        }
        if (updateGroupDto.getDescription() != null) {
            group.setDescription(updateGroupDto.getDescription());
        }
        if (updateGroupDto.getProfileImageUrl() != null) {
            group.setProfileImageUrl(updateGroupDto.getProfileImageUrl());
        }

        Group updatedGroup = jpaGroupRepository.save(group);
        return Mapper.toGroupDto(updatedGroup);
    }

    @Override
    public void deleteGroup(Integer id) {
        jpaGroupRepository.deleteById(id);
    }

    @Override
    public GroupMemberDto addMember(AddMemberDto addMemberDto) {
        GroupMemberId id = GroupMemberId.builder()
                .groupId(addMemberDto.getGroupId())
                .userId(addMemberDto.getUserId())
                .build();

        GroupMember groupMember = new GroupMember();
        groupMember.setId(id);

        Group group = new Group();
        group.setId(addMemberDto.getGroupId());
        groupMember.setGroup(group);

        User user = new User();
        user.setId(addMemberDto.getUserId());
        groupMember.setUser(user);

        groupMember.setRole(addMemberDto.getRole() != null ? addMemberDto.getRole() : GroupRole.MEMBER);

        GroupMember savedMember = jpaGroupMemberRepository.save(groupMember);
        return Mapper.toGroupMemberDto(savedMember);
    }

    @Override
    public List<GroupMemberDto> findMembersByGroupId(Integer groupId) {
        return Mapper.toGroupMemberDtos(jpaGroupMemberRepository.findByGroup_Id(groupId));
    }

    @Override
    public Optional<GroupMemberDto> findMember(Integer groupId, Integer userId) {
        GroupMemberId id = GroupMemberId.builder()
                .groupId(groupId)
                .userId(userId)
                .build();
        return jpaGroupMemberRepository.findById(id)
                .map(Mapper::toGroupMemberDto);
    }

    @Override
    public GroupMemberDto updateMemberRole(UpdateMemberRoleDto updateMemberRoleDto) {
        GroupMemberId id = GroupMemberId.builder()
                .groupId(updateMemberRoleDto.getGroupId())
                .userId(updateMemberRoleDto.getUserId())
                .build();

        GroupMember groupMember = jpaGroupMemberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Group member not found"));

        groupMember.setRole(updateMemberRoleDto.getRole());

        GroupMember updatedMember = jpaGroupMemberRepository.save(groupMember);
        return Mapper.toGroupMemberDto(updatedMember);
    }

    @Override
    public void removeMember(Integer groupId, Integer userId) {
        GroupMemberId id = GroupMemberId.builder()
                .groupId(groupId)
                .userId(userId)
                .build();
        jpaGroupMemberRepository.deleteById(id);
    }

    @Override
    public boolean isMemberOfGroup(Integer groupId, Integer userId) {
        return jpaGroupMemberRepository.existsByGroup_IdAndUser_Id(groupId, userId);
    }
}
