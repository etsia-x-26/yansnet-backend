package com.etsia.job.infrastructure.controller;

import com.etsia.common.infrastructure.entities.JobOffer;
import com.etsia.job.domain.service.JobService;
import com.etsia.job.infrastructure.controller.dto.CreateJobOfferRequest;
import com.etsia.job.infrastructure.controller.dto.JobResponse;
import com.etsia.job.infrastructure.mapper.JobMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
@Tag(name = "Job Management", description = "Endpoints for posting and managing job offers and internships")
public class JobController {

    private final JobService jobService;
    private final JobMapper jobMapper;

    @Operation(summary = "Create a job offer", description = "Publishes a new job or internship opportunity")
    @ApiResponse(responseCode = "200", description = "Job offer created successfully")
    @PostMapping
    public ResponseEntity<JobResponse> createJobOffer(@RequestBody CreateJobOfferRequest request) {
        JobOffer jobOffer = jobMapper.toEntity(request);
        JobOffer savedJob = jobService.createJobOffer(jobOffer);
        return ResponseEntity.ok(jobMapper.toResponse(savedJob));
    }

    @Operation(summary = "Get a job offer by ID")
    @GetMapping("/{id}")
    public ResponseEntity<JobResponse> getJobOfferById(@PathVariable Integer id) {
        return jobService.getJobOffer(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get all job offers")
    @GetMapping
    public ResponseEntity<Page<JobResponse>> getAllJobOffers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<JobResponse> jobs = jobService.getAllJobOffers(pageable);
        return ResponseEntity.ok(jobs);
    }

    @Operation(summary = "Delete a job offer", description = "Removes a job offer from the system")
    @ApiResponse(responseCode = "204", description = "Job offer deleted successfully")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJobOffer(@PathVariable Integer id) {
        jobService.deleteJobOffer(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Apply to a job", description = "Allows a user to apply for a specific job")
    @ApiResponse(responseCode = "200", description = "Application submitted successfully")
    @PostMapping("/{id}/apply")
    public ResponseEntity<Void> applyToJob(@PathVariable Integer id, @RequestParam Integer userId) {
        jobService.applyToJob(id, userId);
        return ResponseEntity.ok().build();
    }
}
