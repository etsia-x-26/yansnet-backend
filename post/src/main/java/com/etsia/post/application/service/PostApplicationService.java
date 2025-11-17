package com.etsia.post.application.service;

import com.etsia.common.domain.model.PostDto;
import com.etsia.post.application.dto.CreatePostRequest;
import com.etsia.post.domain.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class PostApplicationService {
    private final PostService postService;

    public Page<PostDto> getAllPosts(Pageable pageable) {
        return postService.getAllPosts(pageable);
    }

    public void deletePost(Integer id) {
        postService.deletePost(id);
    }

    public PostDto save(CreatePostRequest request) {
        PostDto postDto = PostDto.builder()
                .content(request.getContent())
                .createdAt(Instant.now())
                .user(postService.findUser(request.getUserId()))
                .media(request.getMedias())
                .build();

        return postService.save(postDto);
    }

    public PostDto update(PostDto postDto) {
        return postService.update(postDto);
    }

}
