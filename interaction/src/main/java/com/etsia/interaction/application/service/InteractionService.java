package com.etsia.interaction.application.service;

import com.etsia.common.infrastructure.entities.*;
import com.etsia.interaction.infrastructure.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InteractionService {

    private final JpaPostLikeRepository postLikeRepository;
    private final JpaPostDislikeRepository postDislikeRepository;
    private final JpaCommentLikeRepository commentLikeRepository;
    private final JpaCommentDislikeRepository commentDislikeRepository;
    private final JpaInteractionPostRepository postRepository;
    private final JpaInteractionCommentRepository commentRepository;
    private final JpaInteractionUserRepository userRepository;

    // --- Post Interactions ---

    @Transactional
    public void likePost(Integer userId, Integer postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        PostLikeId id = PostLikeId.builder().userId(userId).postId(postId).build();

        if (postLikeRepository.existsById(id)) {
            return; // Already liked
        }

        // If disliked, remove dislike first
        PostDislikeId dislikeId = PostDislikeId.builder().userId(userId).postId(postId).build();
        if (postDislikeRepository.existsById(dislikeId)) {
            postDislikeRepository.deleteById(dislikeId);
            post.setTotalDislikes(Math.max(0, post.getTotalDislikes() - 1));
        }

        postLikeRepository.save(PostLike.builder().id(id).user(user).post(post).build());
        post.setTotalLikes(post.getTotalLikes() + 1);
        postRepository.save(post);
    }

    @Transactional
    public void unlikePost(Integer userId, Integer postId) {
        PostLikeId id = PostLikeId.builder().userId(userId).postId(postId).build();
        if (postLikeRepository.existsById(id)) {
            postLikeRepository.deleteById(id);
            Post post = postRepository.findById(postId).orElseThrow();
            post.setTotalLikes(Math.max(0, post.getTotalLikes() - 1));
            postRepository.save(post);
        }
    }

    @Transactional
    public void dislikePost(Integer userId, Integer postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        PostDislikeId id = PostDislikeId.builder().userId(userId).postId(postId).build();

        if (postDislikeRepository.existsById(id)) {
            return; // Already disliked
        }

        // If liked, remove like first
        PostLikeId likeId = PostLikeId.builder().userId(userId).postId(postId).build();
        if (postLikeRepository.existsById(likeId)) {
            postLikeRepository.deleteById(likeId);
            post.setTotalLikes(Math.max(0, post.getTotalLikes() - 1));
        }

        postDislikeRepository.save(PostDislike.builder().id(id).user(user).post(post).build());
        post.setTotalDislikes(post.getTotalDislikes() + 1);
        postRepository.save(post);
    }

    @Transactional
    public void undislikePost(Integer userId, Integer postId) {
        PostDislikeId id = PostDislikeId.builder().userId(userId).postId(postId).build();
        if (postDislikeRepository.existsById(id)) {
            postDislikeRepository.deleteById(id);
            Post post = postRepository.findById(postId).orElseThrow();
            post.setTotalDislikes(Math.max(0, post.getTotalDislikes() - 1));
            postRepository.save(post);
        }
    }

    // --- Comment Interactions ---

    @Transactional
    public void likeComment(Integer userId, Integer commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new RuntimeException("Comment not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        CommentLikeId id = CommentLikeId.builder().userId(userId).commentId(commentId).build();

        if (commentLikeRepository.existsById(id)) {
            return;
        }

        CommentDislikeId dislikeId = CommentDislikeId.builder().userId(userId).commentId(commentId).build();
        if (commentDislikeRepository.existsById(dislikeId)) {
            commentDislikeRepository.deleteById(dislikeId);
            comment.setTotalDislikes(Math.max(0, comment.getTotalDislikes() - 1));
        }

        commentLikeRepository.save(CommentLike.builder().id(id).user(user).comment(comment).build());
        comment.setTotalLikes(comment.getTotalLikes() + 1);
        commentRepository.save(comment);
    }

    @Transactional
    public void unlikeComment(Integer userId, Integer commentId) {
        CommentLikeId id = CommentLikeId.builder().userId(userId).commentId(commentId).build();
        if (commentLikeRepository.existsById(id)) {
            commentLikeRepository.deleteById(id);
            Comment comment = commentRepository.findById(commentId).orElseThrow();
            comment.setTotalLikes(Math.max(0, comment.getTotalLikes() - 1));
            commentRepository.save(comment);
        }
    }

    @Transactional
    public void dislikeComment(Integer userId, Integer commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new RuntimeException("Comment not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        CommentDislikeId id = CommentDislikeId.builder().userId(userId).commentId(commentId).build();

        if (commentDislikeRepository.existsById(id)) {
            return;
        }

        CommentLikeId likeId = CommentLikeId.builder().userId(userId).commentId(commentId).build();
        if (commentLikeRepository.existsById(likeId)) {
            commentLikeRepository.deleteById(likeId);
            comment.setTotalLikes(Math.max(0, comment.getTotalLikes() - 1));
        }

        commentDislikeRepository.save(CommentDislike.builder().id(id).user(user).comment(comment).build());
        comment.setTotalDislikes(comment.getTotalDislikes() + 1);
        commentRepository.save(comment);
    }

    @Transactional
    public void undislikeComment(Integer userId, Integer commentId) {
        CommentDislikeId id = CommentDislikeId.builder().userId(userId).commentId(commentId).build();
        if (commentDislikeRepository.existsById(id)) {
            commentDislikeRepository.deleteById(id);
            Comment comment = commentRepository.findById(commentId).orElseThrow();
            comment.setTotalDislikes(Math.max(0, comment.getTotalDislikes() - 1));
            commentRepository.save(comment);
        }
    }
}
