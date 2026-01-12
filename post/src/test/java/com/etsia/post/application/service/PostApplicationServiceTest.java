package com.etsia.post.application.service;

import com.etsia.common.domain.model.PostDto;
import com.etsia.common.domain.model.UserDto;
import com.etsia.post.application.dto.CreatePostRequest;
import com.etsia.post.domain.service.PostService;
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
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostApplicationServiceTest {

    @Mock
    private PostService postService;

    @InjectMocks
    private PostApplicationService postApplicationService;

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
        when(postService.getAllPosts(pageable)).thenReturn(expectedPage);

        // Act
        Page<PostDto> result = postApplicationService.getAllPosts(pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(testPost.getContent(), result.getContent().get(0).getContent());
        verify(postService, times(1)).getAllPosts(pageable);
    }

    @Test
    void getAllPosts_shouldReturnEmptyPage_whenNoPosts() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<PostDto> emptyPage = new PageImpl<>(Collections.emptyList());
        when(postService.getAllPosts(pageable)).thenReturn(emptyPage);

        // Act
        Page<PostDto> result = postApplicationService.getAllPosts(pageable);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        verify(postService, times(1)).getAllPosts(pageable);
    }

    @Test
    void deletePost_shouldCallPostServiceDelete() {
        // Arrange
        Integer postId = 1;
        doNothing().when(postService).deletePost(postId);

        // Act
        postApplicationService.deletePost(postId);

        // Assert
        verify(postService, times(1)).deletePost(postId);
    }

    @Test
    void save_shouldCreateAndSavePost() {
        // Arrange
        CreatePostRequest request = new CreatePostRequest("New post content", 1, null);
        when(postService.findUser(1)).thenReturn(testUser);
        when(postService.save(any(PostDto.class))).thenReturn(testPost);

        // Act
        PostDto result = postApplicationService.save(request);

        // Assert
        assertNotNull(result);
        assertEquals(testPost.getContent(), result.getContent());
        verify(postService, times(1)).findUser(1);
        verify(postService, times(1)).save(any(PostDto.class));
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
        when(postService.update(any(PostDto.class))).thenReturn(updatedPost);

        // Act
        PostDto result = postApplicationService.update(updatedPost);

        // Assert
        assertNotNull(result);
        assertEquals("Updated content", result.getContent());
        verify(postService, times(1)).update(updatedPost);
    }
}
