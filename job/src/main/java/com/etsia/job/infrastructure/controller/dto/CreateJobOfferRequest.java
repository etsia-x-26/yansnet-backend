package com.etsia.job.infrastructure.controller.dto;

import com.etsia.common.infrastructure.security.UserIdAware;
import com.etsia.common.infrastructure.entities.JobType;
import lombok.Data;
import java.time.Instant;

/**
 * Request DTO for creating a new job offer.
 * Implements UserIdAware to automatically receive the publisherId from the JWT token.
 */
@Data
public class CreateJobOfferRequest implements UserIdAware {
    private String title;
    private String description;
    private JobType type;
    private String location;
    private String salary;
    private Instant deadline;
    private String applicationUrl;
    private Integer publisherId;

    @Override
    public void setUserId(Integer userId) {
        this.publisherId = userId;
    }

    @Override
    public Integer getUserId() {
        return this.publisherId;
    }
}
