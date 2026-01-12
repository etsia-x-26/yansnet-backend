package com.etsia.interaction.infrastructure.controller;

import com.etsia.common.infrastructure.security.AuthenticatedUser;
import com.etsia.common.infrastructure.security.CurrentUser;
import com.etsia.interaction.application.service.InteractionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/interactions/posts")
@RequiredArgsConstructor
@Tag(name = "Post Interactions", description = "Endpoints for liking and disliking posts")
public class PostInteractionController {

    private final InteractionService interactionService;

    @Operation(summary = "Like a post")
    @PostMapping("/{id}/like")
    public ResponseEntity<Void> likePost(@PathVariable Integer id, @CurrentUser AuthenticatedUser user) {
        interactionService.likePost(user.getUserId(), id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Unlike a post")
    @DeleteMapping("/{id}/unlike")
    public ResponseEntity<Void> unlikePost(@PathVariable Integer id, @CurrentUser AuthenticatedUser user) {
        interactionService.unlikePost(user.getUserId(), id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Dislike a post")
    @PostMapping("/{id}/dislike")
    public ResponseEntity<Void> dislikePost(@PathVariable Integer id, @CurrentUser AuthenticatedUser user) {
        interactionService.dislikePost(user.getUserId(), id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Undislike a post")
    @DeleteMapping("/{id}/undislike")
    public ResponseEntity<Void> undislikePost(@PathVariable Integer id, @CurrentUser AuthenticatedUser user) {
        interactionService.undislikePost(user.getUserId(), id);
        return ResponseEntity.ok().build();
    }
}
