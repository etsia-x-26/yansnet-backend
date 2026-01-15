package com.etsia.post.domain.service;

import com.etsia.common.domain.model.PostDto;
import com.etsia.common.domain.model.UserDto;
import com.etsia.post.domain.repository.PostRepository;
import com.etsia.post.infrastructure.adapters.PostRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final PostRepositoryImpl postRepositoryImpl;
    private final com.etsia.interaction.domain.service.FollowDomainService followDomainService;

    @Cacheable(value = "posts")
    public Page<PostDto> getAllPosts(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    @Cacheable(value = "posts", key = "#id")
    public Optional<PostDto> getPostById(Integer id) {
        return postRepository.findById(id);
    }

    public Page<PostDto> getPostsByUserId(Integer userId, Pageable pageable) {
        return postRepositoryImpl.findByUserId(userId, pageable);
    }

    public Page<PostDto> getFeedPosts(Integer userId, Pageable pageable) {
        java.util.List<Integer> followedUserIds = followDomainService.getFollowedUserIds(userId);
        if (followedUserIds.isEmpty()) {
            return Page.empty(pageable);
        }
        return postRepositoryImpl.findPostsByAuthorIds(followedUserIds, pageable);
    }

    public Page<PostDto> searchPosts(String query, Pageable pageable) {
        return postRepositoryImpl.searchByContent(query, pageable);
    }

    public void deletePost(Integer id) {
        postRepository.delete(id);
    }

    public PostDto save(PostDto post) {
        PostDto savedPost = postRepository.save(post);
        if (savedPost.getUser() != null && savedPost.getUser().getId() != null) {
            postRepository.incrementPostCount(savedPost.getUser().getId());
        }
        return savedPost;
    }

    public UserDto findUser(Integer userId){
        return  postRepository.findUserById(userId);
    }

    public PostDto update(PostDto post) {
        return postRepository.update(post);
    }
}