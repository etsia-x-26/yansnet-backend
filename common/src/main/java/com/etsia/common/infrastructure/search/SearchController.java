package com.etsia.common.infrastructure.search;

import com.etsia.common.infrastructure.security.AuthenticatedUser;
import com.etsia.common.infrastructure.security.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
@Tag(name = "Search", description = "Global search endpoints for users, posts, events, and jobs")
public class SearchController {

    private final SearchService searchService;

    @Operation(summary = "Global search", description = "Search across all content types (users, posts, events, jobs)")
    @ApiResponse(responseCode = "200", description = "Search results returned successfully")
    @GetMapping
    public ResponseEntity<GlobalSearchResponse> globalSearch(
            @CurrentUser AuthenticatedUser user,
            @Parameter(description = "Search query") @RequestParam String q,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        GlobalSearchResponse response = searchService.globalSearch(q, pageable);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Search by type", description = "Search within a specific content type")
    @ApiResponse(responseCode = "200", description = "Search results returned successfully")
    @GetMapping("/{type}")
    public ResponseEntity<Page<SearchResult>> searchByType(
            @Parameter(description = "Content type: USER, POST, EVENT, JOB") @PathVariable String type,
            @Parameter(description = "Search query") @RequestParam String q,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<SearchResult> results = searchService.searchByType(q, type, pageable);
        return ResponseEntity.ok(results);
    }

    @Operation(summary = "Search users", description = "Search users by name, username, or bio")
    @GetMapping("/users")
    public ResponseEntity<Page<SearchResult>> searchUsers(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return searchByType("USER", q, page, size);
    }

    @Operation(summary = "Search posts", description = "Search posts by content")
    @GetMapping("/posts")
    public ResponseEntity<Page<SearchResult>> searchPosts(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return searchByType("POST", q, page, size);
    }

    @Operation(summary = "Search events", description = "Search events by title, description, category, or location")
    @GetMapping("/events")
    public ResponseEntity<Page<SearchResult>> searchEvents(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return searchByType("EVENT", q, page, size);
    }

    @Operation(summary = "Search jobs", description = "Search job offers by title, description, or location")
    @GetMapping("/jobs")
    public ResponseEntity<Page<SearchResult>> searchJobs(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return searchByType("JOB", q, page, size);
    }
}
