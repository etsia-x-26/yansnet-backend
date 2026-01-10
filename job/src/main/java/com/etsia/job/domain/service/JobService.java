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
public class JobService {

    private final JobRepository jobRepository;

    public JobOffer createJobOffer(JobOffer jobOffer) {
        return jobRepository.save(jobOffer);
    }

    public Optional<JobOffer> getJobOffer(Integer id) {
        return jobRepository.findById(id);
    }

    public Page<JobOffer> getAllJobOffers(Pageable pageable) {
        return jobRepository.findAll(pageable);
    }

    public void deleteJobOffer(Integer id) {
        jobRepository.deleteById(id);
    }
}
