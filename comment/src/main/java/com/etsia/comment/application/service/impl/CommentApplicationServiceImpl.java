package com.etsia.comment.application.service.impl;

import com.etsia.comment.application.dto.CreateCommentRequest;
import com.etsia.comment.application.service.CommentApplicationService;
import com.etsia.common.domain.model.CommentDto;
import com.etsia.common.infrastructure.config.Mapper;
import com.etsia.common.infrastructure.entities.Comment;
import com.etsia.comment.domain.repository.CommentRepository;
import com.etsia.common.infrastructure.entities.Post;
import com.etsia.common.infrastructure.entities.User;
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
                .map(Mapper::toCommentDto);
    }

    @Override
    public CommentDto save(CreateCommentRequest req) {
        Comment comment = Comment.builder()
                .content(req.getContent())
                .user(User.builder().id(req.getUserId()).build())
                .post(Post.builder().id(req.getPostId()).build())
                .build();
        return Mapper.toCommentDto(commentRepository.save(comment));
    }

    @Override
    public CommentDto update(CommentDto dto) {
        Comment existing = commentRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        existing.setContent(dto.getContent());
        return Mapper.toCommentDto(commentRepository.save(existing));
    }

    @Override
    public void deleteComment(Integer id) {
        commentRepository.deleteById(id);
    }
}
