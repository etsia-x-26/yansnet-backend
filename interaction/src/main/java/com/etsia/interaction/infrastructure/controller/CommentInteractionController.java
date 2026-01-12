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
@RequestMapping("/api/interactions/comments")
@RequiredArgsConstructor
@Tag(name = "Comment Interactions", description = "Endpoints for liking and disliking comments")
public class CommentInteractionController {

    private final InteractionService interactionService;

    @Operation(summary = "Like a comment")
    @PostMapping("/{id}/like")
    public ResponseEntity<Void> likeComment(@PathVariable Integer id, @CurrentUser AuthenticatedUser user) {
        interactionService.likeComment(user.getUserId(), id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Unlike a comment")
    @DeleteMapping("/{id}/unlike")
    public ResponseEntity<Void> unlikeComment(@PathVariable Integer id, @CurrentUser AuthenticatedUser user) {
        interactionService.unlikeComment(user.getUserId(), id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Dislike a comment")
    @PostMapping("/{id}/dislike")
    public ResponseEntity<Void> dislikeComment(@PathVariable Integer id, @CurrentUser AuthenticatedUser user) {
        interactionService.dislikeComment(user.getUserId(), id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Undislike a comment")
    @DeleteMapping("/{id}/undislike")
    public ResponseEntity<Void> undislikeComment(@PathVariable Integer id, @CurrentUser AuthenticatedUser user) {
        interactionService.undislikeComment(user.getUserId(), id);
        return ResponseEntity.ok().build();
    }
}
