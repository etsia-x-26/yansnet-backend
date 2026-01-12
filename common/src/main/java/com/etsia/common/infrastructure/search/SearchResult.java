package com.etsia.common.infrastructure.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchResult {
    private Integer id;
    private String type; // USER, POST, EVENT, JOB
    private String title;
    private String description;
    private String imageUrl;
    private Instant createdAt;
    private Object metadata;
}
