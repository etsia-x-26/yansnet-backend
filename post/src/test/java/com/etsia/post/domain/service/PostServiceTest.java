package com.etsia.post.domain.service;

import com.etsia.common.domain.model.PostDto;
import com.etsia.common.domain.model.UserDto;
import com.etsia.post.domain.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostService postService;

    private UserDto testUser;
    private PostDto testPost;

    @BeforeEach
    void setUp() {
        testUser = UserDto.builder()
                .id(1)
                .name("Test User")
                .username("testuser")
                .build();

        testPost = PostDto.builder()
                .id(1)
                .content("Test content")
                .createdAt(Instant.now())
                .user(testUser)
                .build();
    }

    @Test
    void getAllPosts_shouldReturnPageOfPosts() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<PostDto> expectedPage = new PageImpl<>(List.of(testPost));
        when(postRepository.findAll(pageable)).thenReturn(expectedPage);

        // Act
        Page<PostDto> result = postService.getAllPosts(pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(postRepository, times(1)).findAll(pageable);
    }

    @Test
    void deletePost_shouldCallRepositoryDelete() {
        // Arrange
        Integer postId = 1;
        doNothing().when(postRepository).delete(postId);

        // Act
        postService.deletePost(postId);

        // Assert
        verify(postRepository, times(1)).delete(postId);
    }

    @Test
    void save_shouldSavePostAndIncrementPostCount() {
        // Arrange
        when(postRepository.save(any(PostDto.class))).thenReturn(testPost);
        doNothing().when(postRepository).incrementPostCount(anyInt());

        // Act
        PostDto result = postService.save(testPost);

        // Assert
        assertNotNull(result);
        assertEquals(testPost.getContent(), result.getContent());
        verify(postRepository, times(1)).save(testPost);
        verify(postRepository, times(1)).incrementPostCount(testUser.getId());
    }

    @Test
    void save_shouldNotIncrementPostCount_whenUserIsNull() {
        // Arrange
        PostDto postWithoutUser = PostDto.builder()
                .id(2)
                .content("Post without user")
                .createdAt(Instant.now())
                .user(null)
                .build();
        when(postRepository.save(any(PostDto.class))).thenReturn(postWithoutUser);

        // Act
        PostDto result = postService.save(postWithoutUser);

        // Assert
        assertNotNull(result);
        verify(postRepository, times(1)).save(postWithoutUser);
        verify(postRepository, never()).incrementPostCount(anyInt());
    }

    @Test
    void findUser_shouldReturnUser() {
        // Arrange
        when(postRepository.findUserById(1)).thenReturn(testUser);

        // Act
        UserDto result = postService.findUser(1);

        // Assert
        assertNotNull(result);
        assertEquals(testUser.getId(), result.getId());
        verify(postRepository, times(1)).findUserById(1);
    }

    @Test
    void update_shouldUpdatePost() {
        // Arrange
        PostDto updatedPost = PostDto.builder()
                .id(1)
                .content("Updated content")
                .createdAt(Instant.now())
                .user(testUser)
                .build();
        when(postRepository.update(any(PostDto.class))).thenReturn(updatedPost);

        // Act
        PostDto result = postService.update(updatedPost);

        // Assert
        assertNotNull(result);
        assertEquals("Updated content", result.getContent());
        verify(postRepository, times(1)).update(updatedPost);
    }
}
