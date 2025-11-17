package com.etsia.comment.application.service.impl;

import com.etsia.comment.application.dto.CommentDto;
import com.etsia.comment.application.dto.CreateCommentRequest;
import com.etsia.comment.application.service.CommentApplicationService;
import com.etsia.comment.domain.model.Comment;
import com.etsia.comment.domain.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentApplicationServiceImpl implements CommentApplicationService {

    private final CommentRepository commentRepository;

    @Override
    public Page<CommentDto> getAllComments(Integer postId, Pageable pageable) {
        return commentRepository.findByPostId(postId, pageable)
                .map(Comment::toDto);
    }

    @Override
    public CommentDto save(CreateCommentRequest req) {
        Comment comment = Comment.fromRequest(req);
        return commentRepository.save(comment).toDto();
    }

    @Override
    public CommentDto update(CommentDto dto) {
        Comment existing = commentRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        existing.setContent(dto.getContent());
        return commentRepository.save(existing).toDto();
    }

    @Override
    public void deleteComment(Integer id) {
        commentRepository.deleteById(id);
    }
}
