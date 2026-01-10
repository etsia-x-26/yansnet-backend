package com.etsia.interaction.infrastructure.controller.conversation;

import com.etsia.common.domain.model.ConversationUserDto;
import com.etsia.common.domain.model.sub.ConversationRole;
import com.etsia.interaction.application.service.conversation.*;
import com.etsia.interaction.domain.model.conversation.FindAllConversationByUserDto;
import com.etsia.interaction.domain.model.conversation.FindAllUserByConversationDto;
import com.etsia.interaction.domain.model.conversation.FollowConversationDto;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/Conversation/user")
@CrossOrigin("*")
@AllArgsConstructor
public class FollowConversationController {

    private final FindConversationUserUseCase findConversationUserUseCase;
    private final FollowConversationUseCase followConversationUseCase;
    private final UnFollowConversationUserUseCase unFollowConversationUserUseCase;
    private final FindAllConversationByUser findAllConversationByUser;
    private final FindAllUserByConversation findAllUserByConversation;
    private final FindAllUserByConversationAndRole findAllUserByConversationAndRole;

    @PostMapping("/follow/{ConversationId}")
    ResponseEntity<FindAllUserByConversationDto> follow(@PathVariable Integer ConversationId,
            @RequestBody FollowConversationDto followConversationDto) {
        try {
            // Execute the use case to add followers with their respective roles
            followConversationUseCase.execute(followConversationDto.getFollowerRoles(), ConversationId);
            // After following, fetch the updated conversation with user list
            FindAllUserByConversationDto result = findAllUserByConversation.executeSingle(ConversationId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/user/{FollowerId}")
    public ResponseEntity<List<FindAllConversationByUserDto>> findConversationUserFollowers(@PathVariable Integer FollowerId) {
        try {
            List<FindAllConversationByUserDto> result = findAllConversationByUser.execute(FollowerId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/conversation/{ConversationId}")
    ResponseEntity<List<FindAllUserByConversationDto>> findUserConversationFollowers(@PathVariable Integer ConversationId) {
        try {
            List<FindAllUserByConversationDto> result = findAllUserByConversation.execute(ConversationId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**@GetMapping("/role/{ConversationId}")
    public ResponseEntity<List<ConversationUserDto>> findUserConversationRoleFollowers(
            @PathVariable Integer ConversationId,
            @RequestBody ConversationRole role) {
        try {
            List<ConversationUserDto> result = findAllUserByConversationAndRole.execute(ConversationId, role);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/follow/{ConversationId}")
    ResponseEntity<ConversationUserDto> conversationFollow(@PathVariable Integer ConversationId) {
        try {
            Optional<ConversationUserDto> result = findConversationUserUseCase.execute(ConversationId);
            return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
     */

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
