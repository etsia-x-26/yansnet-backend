package com.etsia.common.infrastructure.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GlobalSearchResponse {
    private List<SearchResult> users;
    private List<SearchResult> posts;
    private List<SearchResult> events;
    private List<SearchResult> jobs;
    private int totalResults;
    private String query;
}
