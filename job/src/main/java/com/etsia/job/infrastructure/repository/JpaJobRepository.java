package com.etsia.job.infrastructure.repository;

import com.etsia.common.infrastructure.entities.JobOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaJobRepository extends JpaRepository<JobOffer, Integer> {
}
