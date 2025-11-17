package com.etsia.comment.application.service;

import com.etsia.comment.application.dto.CreateCommentRequest;
import com.etsia.common.domain.model.CommentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentApplicationService {

    Page<CommentDto> getAllComments(Integer postId, Pageable pageable);

    CommentDto save(CreateCommentRequest request);

    CommentDto update(CommentDto dto);

    void deleteComment(Integer id);
}
