package com.etsia.job.infrastructure.controller.dto;

import com.etsia.common.infrastructure.entities.JobType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JobResponse {
    private Integer id;
    private String title;
    private String description;
    private JobType type;
    private String location;
    private String salary;
    private Instant deadline;
    private String applicationUrl;
    private Integer publisherId;
    private String publisherName;
    private Instant createdAt;
}
