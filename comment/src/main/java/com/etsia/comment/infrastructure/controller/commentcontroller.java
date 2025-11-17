package com.etsia.comment.infrastructure.controller;

import com.etsia.comment.application.dto.CreateCommentRequest;
import com.etsia.comment.application.dto.CommentDto;
import com.etsia.comment.application.dto.PageResponse;
import com.etsia.comment.application.service.CommentApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentApplicationService commentService;

    @GetMapping
    public ResponseEntity<PageResponse<CommentDto>> getAllComments(
            @RequestParam Integer postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction
    ) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        PageRequest pageRequest = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(PageResponse.from(commentService.getAllComments(postId, pageRequest)));
    }

    @PostMapping
    public ResponseEntity<CommentDto> addComment(@RequestBody CreateCommentRequest request) {
        return ResponseEntity.ok(commentService.save(request));
    }

    @PatchMapping
    public ResponseEntity<CommentDto> updateComment(@RequestBody CommentDto commentDto) {
        return ResponseEntity.ok(commentService.update(commentDto));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteComment(@RequestParam Integer id) {
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }
}
