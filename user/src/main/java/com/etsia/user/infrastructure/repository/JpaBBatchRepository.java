package com.etsia.user.infrastructure.repository;

import com.etsia.common.infrastructure.entities.Batch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository(value = "UBRepository")
public interface JpaBBatchRepository extends JpaRepository<Batch , Integer> {
}
