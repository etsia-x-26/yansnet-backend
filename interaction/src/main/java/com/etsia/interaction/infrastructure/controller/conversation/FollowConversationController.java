package com.etsia.interaction.infrastructure.controller.conversation;

import com.etsia.common.domain.model.sub.ConversationRole;
import com.etsia.interaction.application.service.conversation.FollowConversationUseCase;
import com.etsia.interaction.application.service.conversation.UnFollowConversationUserUseCase;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/followConversation")
@CrossOrigin("*")
@AllArgsConstructor
public class FollowConversationController {

    private final FollowConversationUseCase followConversationUseCase;
    private final UnFollowConversationUserUseCase unFollowConversationUserUseCase;

    @PostMapping("/follow/{ConversationId}/{FollowerId}")
    ResponseEntity follow(@PathVariable int FollowerId, @PathVariable int ConversationId, @RequestBody ConversationRole role) {
        try {
            followConversationUseCase.execute(FollowerId, ConversationId, role);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/unfollow/{ConversationId}/{FollowerId}")
    ResponseEntity unfollow(@PathVariable int FollowerId, @PathVariable int ConversationId) {
        try {
            unFollowConversationUserUseCase.execute(FollowerId, ConversationId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
