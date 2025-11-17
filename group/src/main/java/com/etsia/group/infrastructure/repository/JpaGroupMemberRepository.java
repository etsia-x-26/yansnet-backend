package com.etsia.group.infrastructure.repository;

import com.etsia.common.infrastructure.entities.GroupMember;
import com.etsia.common.infrastructure.entities.GroupMemberId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository(value = "gJGroupMemberRepository")
public interface JpaGroupMemberRepository extends JpaRepository<GroupMember, GroupMemberId> {
    List<GroupMember> findByGroup_Id(Integer groupId);
    boolean existsByGroup_IdAndUser_Id(Integer groupId, Integer userId);
}
