package com.etsia.job.infrastructure.controller;

import com.etsia.common.infrastructure.entities.JobOffer;
import com.etsia.job.domain.service.JobService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
@Tag(name = "Job Management", description = "Endpoints for posting and managing job offers and internships")
public class JobController {

    private final JobService jobService;

    @Operation(summary = "Create a job offer", description = "Publishes a new job or internship opportunity")
    @ApiResponse(responseCode = "200", description = "Job offer created successfully")
    @PostMapping
    public ResponseEntity<JobOffer> createJobOffer(@RequestBody JobOffer jobOffer) {
        return ResponseEntity.ok(jobService.createJobOffer(jobOffer));
    }

    @Operation(summary = "Get job offer by ID", description = "Retrieves details of a specific job offer")
    @ApiResponse(responseCode = "200", description = "Job offer found")
    @ApiResponse(responseCode = "404", description = "Job offer not found")
    @GetMapping("/{id}")
    public ResponseEntity<JobOffer> getJobOffer(@PathVariable Integer id) {
        return jobService.getJobOffer(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get all job offers", description = "Retrieves a paginated list of all active job offers")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of job offers")
    @GetMapping
    public ResponseEntity<Page<JobOffer>> getAllJobOffers(Pageable pageable) {
        return ResponseEntity.ok(jobService.getAllJobOffers(pageable));
    }

    @Operation(summary = "Delete a job offer", description = "Removes a job offer from the system")
    @ApiResponse(responseCode = "204", description = "Job offer deleted successfully")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJobOffer(@PathVariable Integer id) {
        jobService.deleteJobOffer(id);
        return ResponseEntity.noContent().build();
    }
}
