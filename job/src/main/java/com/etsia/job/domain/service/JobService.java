package com.etsia.job.domain.service;

import com.etsia.common.infrastructure.entities.JobOffer;
import com.etsia.job.domain.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@lombok.extern.slf4j.Slf4j
public class JobService {

    private final JobRepository jobRepository;

    public JobOffer createJobOffer(JobOffer jobOffer) {
        log.info("Creating job offer: {}", jobOffer.getTitle());
        return jobRepository.save(jobOffer);
    }

    public Optional<JobOffer> getJobOffer(Integer id) {
        return jobRepository.findById(id);
    }

    public Page<JobOffer> getAllJobOffers(Pageable pageable) {
        Page<JobOffer> jobs = jobRepository.findAll(pageable);
        log.info("Retrieved {} jobs from database", jobs.getTotalElements());
        return jobs;
    }

    public void deleteJobOffer(Integer id) {
        jobRepository.deleteById(id);
    }
}
