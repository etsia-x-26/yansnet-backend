package com.etsia.post.infrastructure.controller;

import com.etsia.common.domain.model.PostDto;
import com.etsia.common.infrastructure.security.AuthenticatedUser;
import com.etsia.common.infrastructure.security.CurrentUser;
import com.etsia.post.application.dto.CreatePostRequest;
import com.etsia.post.application.dto.PageResponse;
import com.etsia.post.application.service.PostApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@Tag(name = "Post Management", description = "Endpoints for creating, retrieving, and managing social posts")
public class PostController {

    private final PostApplicationService postService;

    @Operation(summary = "Get all posts", description = "Retrieves a paginated list of social posts")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved posts")
    @GetMapping
    public ResponseEntity<PageResponse<PostDto>> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction
    ) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        PageRequest pageRequest = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(PageResponse.from(postService.getAllPosts(pageRequest)));
    }

    @Operation(summary = "Get my posts", description = "Retrieves all posts from the authenticated user")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved user posts")
    @GetMapping("/me")
    public ResponseEntity<PageResponse<PostDto>> getMyPosts(
            @CurrentUser AuthenticatedUser user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return ResponseEntity.ok(PageResponse.from(postService.getPostsByUserId(user.getUserId(), pageRequest)));
    }

    @Operation(summary = "Get post by ID", description = "Retrieves a single post by its ID")
    @ApiResponse(responseCode = "200", description = "Post found")
    @ApiResponse(responseCode = "404", description = "Post not found")
    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(
            @Parameter(description = "Post ID") @PathVariable Integer id) {
        return postService.getPostById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get posts by user", description = "Retrieves all posts from a specific user")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved user posts")
    @GetMapping("/user/{userId}")
    public ResponseEntity<PageResponse<PostDto>> getPostsByUser(
            @Parameter(description = "User ID") @PathVariable Integer userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return ResponseEntity.ok(PageResponse.from(postService.getPostsByUserId(userId, pageRequest)));
    }

    @Operation(summary = "Search posts", description = "Search posts by content")
    @ApiResponse(responseCode = "200", description = "Search results returned")
    @GetMapping("/search")
    public ResponseEntity<PageResponse<PostDto>> searchPosts(
            @Parameter(description = "Search query") @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return ResponseEntity.ok(PageResponse.from(postService.searchPosts(q, pageRequest)));
    }

    @Operation(summary = "Delete a post", description = "Permanently removes a post by its ID")
    @ApiResponse(responseCode = "204", description = "Post deleted successfully")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(
            @PathVariable Integer id,
            @CurrentUser AuthenticatedUser user) {
        // TODO: Add ownership check
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Create a new post", description = "Submits a new post with content and optional media")
    @ApiResponse(responseCode = "200", description = "Post created successfully")
    @PostMapping
    public ResponseEntity<PostDto> save(@RequestBody CreatePostRequest request) {
        return ResponseEntity.ok(postService.save(request));
    }

    @Operation(summary = "Update a post", description = "Updates the content of an existing post")
    @ApiResponse(responseCode = "200", description = "Post updated successfully")
    @PatchMapping
    public ResponseEntity<PostDto> update(
            @CurrentUser AuthenticatedUser user,
            @RequestBody PostDto postDto) {
        // TODO: Add ownership check
        return ResponseEntity.ok(postService.update(postDto));
    }
}
