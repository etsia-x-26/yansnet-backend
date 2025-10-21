package com.etsia.post.infrastructure.controller;

import com.etsia.common.domain.model.PostDto;
import com.etsia.post.application.dto.CreatePostRequest;
import com.etsia.post.application.dto.PageResponse;
import com.etsia.post.application.service.PostApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostApplicationService postService;

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

    @DeleteMapping
    public ResponseEntity<Void> deletePost(@RequestParam Integer id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping()
    public ResponseEntity<PostDto> save(@RequestBody CreatePostRequest request) {
        return ResponseEntity.ok(postService.save(request));
    }

    @PatchMapping
    public ResponseEntity<PostDto> update(@RequestBody PostDto postDto) {
        return ResponseEntity.ok(postService.update(postDto));
    }

}
