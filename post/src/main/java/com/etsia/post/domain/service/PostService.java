package com.etsia.post.domain.service;

import com.etsia.common.domain.model.PostDto;
import com.etsia.common.domain.model.UserDto;
import com.etsia.post.domain.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public Page<PostDto> getAllPosts(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    public void deletePost(Integer id) {
        postRepository.delete(id);
    }

    public PostDto save(PostDto post) {
        return postRepository.save(post);
    }

    public UserDto findUser(Integer userId){
        return  postRepository.findUserById(userId);
    }

    public PostDto update(PostDto post) {
        return postRepository.update(post);
    }
}