package com.etsia.common.domain.model;

import lombok.Builder;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.etsia.common.infrastructure.entities.Batch}
 */


@Builder
@Value
public class BatchDto implements Serializable {
    Integer id;
    String departement;
    Integer endYear;
}