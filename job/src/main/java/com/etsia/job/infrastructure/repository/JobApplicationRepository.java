package com.etsia.job.infrastructure.repository;

import com.etsia.common.infrastructure.entities.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Integer> {
    List<JobApplication> findByUserId(Integer userId);
    List<JobApplication> findByJobId(Integer jobId);
    Optional<JobApplication> findByJobIdAndUserId(Integer jobId, Integer userId);
    boolean existsByJobIdAndUserId(Integer jobId, Integer userId);
}
