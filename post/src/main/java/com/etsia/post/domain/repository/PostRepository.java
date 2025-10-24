package com.etsia.post.domain.repository;

import com.etsia.common.domain.model.PostDto;
import com.etsia.common.domain.model.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PostRepository {
    PostDto save(PostDto post);
    Optional<PostDto> findById(Integer id);
    List<PostDto> findByAuthorId(Integer authorId);
    void delete(Integer id);
    Page<PostDto> findAll(Pageable pageable);
    PostDto update(PostDto postDto);

    UserDto findUserById(Integer userId);
}