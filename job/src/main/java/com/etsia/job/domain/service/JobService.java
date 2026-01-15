package com.etsia.job.domain.service;

import com.etsia.common.infrastructure.entities.ApplicationStatus;
import com.etsia.common.infrastructure.entities.JobApplication;
import com.etsia.common.infrastructure.entities.JobOffer;
import com.etsia.common.infrastructure.entities.User;
import com.etsia.common.infrastructure.exception.BusinessException;
import com.etsia.job.infrastructure.repository.JobApplicationRepository;
import com.etsia.job.domain.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@lombok.extern.slf4j.Slf4j
public class JobService {

    private final JobRepository jobRepository;
    private final JobApplicationRepository jobApplicationRepository;

    public JobOffer createJobOffer(JobOffer jobOffer) {
        log.info("Creating job offer: {}", jobOffer.getTitle());
        return jobRepository.save(jobOffer);
    }

    @Cacheable(value = "jobs", key = "#id")
    public Optional<JobOffer> getJobOffer(Integer id) {
        return jobRepository.findById(id);
    }

    @Cacheable(value = "jobs")
    public Page<JobOffer> getAllJobOffers(Pageable pageable) {
        Page<JobOffer> jobs = jobRepository.findAll(pageable);
        log.info("Retrieved {} jobs from database", jobs.getTotalElements());
        return jobs;
    }

    public void deleteJobOffer(Integer id) {
        jobRepository.deleteById(id);
    }

    @Transactional
    public void applyToJob(Integer jobId, Integer userId) {
        if (jobApplicationRepository.existsByJobIdAndUserId(jobId, userId)) {
            throw new BusinessException("You have already applied to this job", "JOB_ALREADY_APPLIED");
        }

        JobApplication application = JobApplication.builder()
                .job(JobOffer.builder().id(jobId).build())
                .user(User.builder().id(userId).build())
                .status(ApplicationStatus.PENDING)
                .appliedAt(java.time.Instant.now())
                .build();

        jobApplicationRepository.save(application);
        log.info("User {} applied to job {}", userId, jobId);
    }
}
