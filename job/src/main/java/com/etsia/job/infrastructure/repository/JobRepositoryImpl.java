package com.etsia.job.infrastructure.repository;

import com.etsia.common.infrastructure.entities.JobOffer;
import com.etsia.job.domain.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JobRepositoryImpl implements JobRepository {

    private final JpaJobRepository jpaJobRepository;

    @Override
    public JobOffer save(JobOffer jobOffer) {
        return jpaJobRepository.save(jobOffer);
    }

    @Override
    public Optional<JobOffer> findById(Integer id) {
        return jpaJobRepository.findById(id);
    }

    @Override
    public void deleteById(Integer id) {
        jpaJobRepository.deleteById(id);
    }

    @Override
    public Page<JobOffer> findAll(Pageable pageable) {
        return jpaJobRepository.findAll(pageable);
    }
}
