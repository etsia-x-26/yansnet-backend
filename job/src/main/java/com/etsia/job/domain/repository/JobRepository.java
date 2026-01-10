package com.etsia.job.domain.repository;

import com.etsia.common.infrastructure.entities.JobOffer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface JobRepository {
    JobOffer save(JobOffer jobOffer);
    Optional<JobOffer> findById(Integer id);
    void deleteById(Integer id);
    Page<JobOffer> findAll(Pageable pageable);
}
