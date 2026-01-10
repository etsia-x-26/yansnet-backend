package com.etsia.group.infrastructure.repository;

import com.etsia.common.infrastructure.entities.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository(value = "gJGroupRepository")
public interface JpaGroupRepository extends JpaRepository<Group, Integer> {
    List<Group> findByCreatedBy_Id(Integer createdById);
}
