package com.etsia.job.infrastructure.controller;

import com.etsia.common.infrastructure.entities.JobOffer;
import com.etsia.job.domain.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @PostMapping
    public ResponseEntity<JobOffer> createJobOffer(@RequestBody JobOffer jobOffer) {
        return ResponseEntity.ok(jobService.createJobOffer(jobOffer));
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobOffer> getJobOffer(@PathVariable Integer id) {
        return jobService.getJobOffer(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<Page<JobOffer>> getAllJobOffers(Pageable pageable) {
        return ResponseEntity.ok(jobService.getAllJobOffers(pageable));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJobOffer(@PathVariable Integer id) {
        jobService.deleteJobOffer(id);
        return ResponseEntity.noContent().build();
    }
}
