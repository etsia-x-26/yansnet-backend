package com.etsia.post.infrastructure.controller;

import com.etsia.common.domain.model.PostDto;
import com.etsia.post.application.dto.CreatePostRequest;
import com.etsia.post.application.dto.PageResponse;
import com.etsia.post.application.service.PostApplicationService;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "Delete a post", description = "Permenantly removes a post by its ID")
    @ApiResponse(responseCode = "204", description = "Post deleted successfully")
    @DeleteMapping
    public ResponseEntity<Void> deletePost(@RequestParam Integer id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Create a new post", description = "Submits a new post with content and optional media")
    @ApiResponse(responseCode = "200", description = "Post created successfully")
    @PostMapping()
    public ResponseEntity<PostDto> save(@RequestBody CreatePostRequest request) {
        return ResponseEntity.ok(postService.save(request));
    }

    @Operation(summary = "Update a post", description = "Updates the content of an existing post")
    @ApiResponse(responseCode = "200", description = "Post updated successfully")
    @PatchMapping
    public ResponseEntity<PostDto> update(@RequestBody PostDto postDto) {
        return ResponseEntity.ok(postService.update(postDto));
    }

}
