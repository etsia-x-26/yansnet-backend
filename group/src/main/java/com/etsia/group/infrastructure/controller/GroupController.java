package com.etsia.group.infrastructure.controller;

import com.etsia.common.domain.model.GroupDto;
import com.etsia.common.domain.model.GroupMemberDto;
import com.etsia.group.application.service.*;
import com.etsia.group.domain.model.dto.request.AddMemberDto;
import com.etsia.group.domain.model.dto.request.CreateGroupDto;
import com.etsia.group.domain.model.dto.request.UpdateGroupDto;
import com.etsia.group.domain.model.dto.request.UpdateMemberRoleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupCreateService groupCreateService;
    private final GroupUpdateService groupUpdateService;
    private final GroupDeleteService groupDeleteService;
    private final GroupFindByIdService groupFindByIdService;
    private final GroupFindAllService groupFindAllService;
    private final GroupMemberAddService groupMemberAddService;
    private final GroupMemberFindService groupMemberFindService;
    private final GroupMemberRemoveService groupMemberRemoveService;
    private final GroupMemberUpdateRoleService groupMemberUpdateRoleService;

    @PostMapping
    public ResponseEntity<GroupDto> createGroup(@RequestBody CreateGroupDto createGroupDto) {
        GroupDto createdGroup = groupCreateService.execute(createGroupDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdGroup);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<GroupDto>> getGroupById(@PathVariable Integer id) {
        Optional<GroupDto> group = groupFindByIdService.execute(id);
        return ResponseEntity.ok(group);
    }

    @GetMapping
    public ResponseEntity<List<GroupDto>> getAllGroups() {
        List<GroupDto> groups = groupFindAllService.execute();
        return ResponseEntity.ok(groups);
    }

    @PutMapping
    public ResponseEntity<GroupDto> updateGroup(@RequestBody UpdateGroupDto updateGroupDto) {
        GroupDto updatedGroup = groupUpdateService.execute(updateGroupDto);
        return ResponseEntity.ok(updatedGroup);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable Integer id) {
        groupDeleteService.execute(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/members")
    public ResponseEntity<GroupMemberDto> addMember(@RequestBody AddMemberDto addMemberDto) {
        GroupMemberDto addedMember = groupMemberAddService.execute(addMemberDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedMember);
    }

    @GetMapping("/{groupId}/members")
    public ResponseEntity<List<GroupMemberDto>> getGroupMembers(@PathVariable Integer groupId) {
        List<GroupMemberDto> members = groupMemberFindService.execute(groupId);
        return ResponseEntity.ok(members);
    }

    @PutMapping("/members/role")
    public ResponseEntity<GroupMemberDto> updateMemberRole(@RequestBody UpdateMemberRoleDto updateMemberRoleDto) {
        GroupMemberDto updatedMember = groupMemberUpdateRoleService.execute(updateMemberRoleDto);
        return ResponseEntity.ok(updatedMember);
    }

    @DeleteMapping("/{groupId}/members/{userId}")
    public ResponseEntity<Void> removeMember(@PathVariable Integer groupId, @PathVariable Integer userId) {
        groupMemberRemoveService.execute(groupId, userId);
        return ResponseEntity.noContent().build();
    }
}
