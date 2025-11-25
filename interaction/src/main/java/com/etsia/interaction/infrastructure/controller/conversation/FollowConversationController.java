package com.etsia.interaction.infrastructure.controller.conversation;

import com.etsia.common.domain.model.ConversationUserDto;
import com.etsia.interaction.application.service.conversation.FindConversationUserUseCase;
import com.etsia.interaction.application.service.conversation.FollowConversationUseCase;
import com.etsia.interaction.application.service.conversation.UnFollowConversationUserUseCase;
import com.etsia.interaction.domain.model.conversation.FollowConversationDto;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/followConversation")
@CrossOrigin("*")
@AllArgsConstructor
public class FollowConversationController {

    private final FindConversationUserUseCase findConversationUserUseCase;
    private final FollowConversationUseCase followConversationUseCase;
    private final UnFollowConversationUserUseCase unFollowConversationUserUseCase;

    @PostMapping("/follow/{ConversationId}")
    ResponseEntity<Void> follow(@PathVariable Integer ConversationId, @RequestBody FollowConversationDto followConversationDto) {
        try {
            followConversationUseCase.execute(followConversationDto.getFollowerIds(), ConversationId, followConversationDto.getRole());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/follow/{ConversationId}")
    ResponseEntity<ConversationUserDto> follow(@PathVariable Integer ConversationId){
        try{
            Optional<ConversationUserDto> result = findConversationUserUseCase.execute(ConversationId);
            if (result.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(result.get());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/unfollow/{ConversationId}/{FollowerId}")
    ResponseEntity<String> unfollow(@PathVariable Integer FollowerId, @PathVariable Integer ConversationId) {
        try {
            unFollowConversationUserUseCase.execute(FollowerId, ConversationId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
