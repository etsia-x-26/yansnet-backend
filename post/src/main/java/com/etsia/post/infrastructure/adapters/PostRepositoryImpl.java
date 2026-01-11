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
    public Optional<PostDto> findById(Integer id) {
        return Optional.empty();
    }

    @Override
    public List<PostDto> findByAuthorId(Integer authorId) {
        return List.of();
    }

    @Override
    public void delete(Integer id) {
        jpaPostRepository.deleteById(id);
    }

    @Override
    public Page<PostDto> findAll(Pageable pageable) {
        return jpaPostRepository.findAll(pageable).map(Mapper::toPostDto);
    }

    @Override
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
}
