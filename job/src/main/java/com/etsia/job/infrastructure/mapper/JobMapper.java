package com.etsia.job.infrastructure.mapper;

import com.etsia.common.infrastructure.entities.JobOffer;
import com.etsia.common.infrastructure.entities.User;
import com.etsia.job.infrastructure.controller.dto.CreateJobOfferRequest;
import com.etsia.job.infrastructure.controller.dto.JobResponse;
import org.springframework.stereotype.Component;
import java.time.Instant;

@Component
public class JobMapper {
    public JobOffer toEntity(CreateJobOfferRequest request) {
        return JobOffer.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .type(request.getType())
                .location(request.getLocation())
                .salary(request.getSalary())
                .deadline(request.getDeadline())
                .applicationUrl(request.getApplicationUrl())
                .publisher(User.builder().id(request.getPublisherId()).build())
                .createdAt(Instant.now())
                .build();
    }

    public JobResponse toResponse(JobOffer entity) {
        return JobResponse.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .type(entity.getType())
                .location(entity.getLocation())
                .salary(entity.getSalary())
                .deadline(entity.getDeadline())
                .applicationUrl(entity.getApplicationUrl())
                .publisherId(entity.getPublisher() != null ? entity.getPublisher().getId() : null)
                .publisherName(entity.getPublisher() != null ? entity.getPublisher().getName() : null)
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
