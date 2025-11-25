package com.etsia.user.infrastructure.repository;

import com.etsia.common.infrastructure.entities.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository(value = "UDJRepository")
public interface JpaDDepartementRepository extends JpaRepository<Department, Integer> {
}
