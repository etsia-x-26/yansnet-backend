package com.etsia.post.infrastructure.adapters;
import com.etsia.common.infrastructure.entities.User;
import lombok.extern.slf4j.Slf4j;

import com.etsia.common.domain.model.PostDto;
import com.etsia.common.domain.model.UserDto;
import com.etsia.common.infrastructure.config.Mapper;
import com.etsia.common.infrastructure.entities.Post;
import com.etsia.post.domain.repository.PostRepository;
import com.etsia.post.infrastructure.repository.JpaPostRepository;
import com.etsia.post.infrastructure.repository.JpaUserPostUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class PostRepositoryImpl implements PostRepository {

    private final JpaPostRepository jpaPostRepository;
    private final JpaUserPostUserRepository jpaUserPostUserRepository;

    @Override
    @CacheEvict(value = {"posts", "search_posts"}, allEntries = true)
    public PostDto save(PostDto postDto) {
        Post post = Mapper.toPostEntity(postDto);
        log.debug("Saving post with content: {}, User ID: {}, Media size: {}", 
                post.getContent(), 
                post.getUser() != null ? post.getUser().getId() : "null",
                post.getMedia() != null ? post.getMedia().size() : 0);
        Post savedPost = jpaPostRepository.save(post);
        return Mapper.toPostDto(savedPost);
    }

    @Override
    @Cacheable(value = "posts", key = "#id")
    public Optional<PostDto> findById(Integer id) {
        return jpaPostRepository.findByIdAndNotDeleted(id)
                .map(Mapper::toPostDto);
    }

    @Override
    public List<PostDto> findByAuthorId(Integer authorId) {
        return List.of();
    }

    @Override
    @CacheEvict(value = {"posts", "search_posts"}, allEntries = true)
    public void delete(Integer id) {
        jpaPostRepository.deleteById(id);
    }

    @Override
    public Page<PostDto> findAll(Pageable pageable) {
        return jpaPostRepository.findAll(pageable).map(Mapper::toPostDto);
    }

    @Override
    @CacheEvict(value = {"posts", "search_posts"}, allEntries = true)
    public PostDto update(PostDto postDto) {
        Post post = jpaPostRepository.save(Mapper.toPostEntity(postDto));
        return Mapper.toPostDto(post);
    }

    @Override
    public UserDto findUserById(Integer userId) {
        return Mapper.toUserDto(this.jpaUserPostUserRepository.findById(userId).orElseThrow());
    }

    @Override
    public void incrementPostCount(Integer userId) {
        User user = this.jpaUserPostUserRepository.findById(userId).orElseThrow();
        user.setTotalPosts(user.getTotalPosts() + 1);
        this.jpaUserPostUserRepository.save(user);
    }
    
    public Page<PostDto> findByUserId(Integer userId, Pageable pageable) {
        return jpaPostRepository.findByUserId(userId, pageable).map(Mapper::toPostDto);
    }
    
    public Page<PostDto> searchByContent(String query, Pageable pageable) {
        return jpaPostRepository.searchByContent(query, pageable).map(Mapper::toPostDto);
    }

    @Override
    public Page<PostDto> findPostsByAuthorIds(List<Integer> authorIds, Pageable pageable) {
        return jpaPostRepository.findByUserIdIn(authorIds, pageable).map(Mapper::toPostDto);
    }
}
