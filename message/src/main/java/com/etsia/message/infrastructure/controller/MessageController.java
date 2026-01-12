package com.etsia.message.infrastructure.controller;

import com.etsia.common.infrastructure.security.AuthenticatedUser;
import com.etsia.common.infrastructure.security.CurrentUser;
import com.etsia.message.domain.model.ConversationDto;
import com.etsia.message.domain.model.MessageDto;
import com.etsia.message.domain.service.MessageService;
import com.etsia.message.infrastructure.controller.dto.CreateConversationRequest;
import com.etsia.message.infrastructure.controller.dto.SendMessageRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
@Tag(name = "Messaging", description = "REST API for conversations and messages")
public class MessageController {

    private final MessageService messageService;

    @Operation(summary = "Get user conversations", description = "Get all conversations for the authenticated user")
    @ApiResponse(responseCode = "200", description = "Conversations retrieved successfully")
    @GetMapping("/conversations")
    public ResponseEntity<Page<ConversationDto>> getConversations(
            @CurrentUser AuthenticatedUser user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(messageService.getUserConversations(user.getUserId(), PageRequest.of(page, size)));
    }

    @Operation(summary = "Get conversation by ID", description = "Get a specific conversation with participants")
    @ApiResponse(responseCode = "200", description = "Conversation found")
    @ApiResponse(responseCode = "404", description = "Conversation not found or user not a participant")
    @GetMapping("/conversations/{conversationId}")
    public ResponseEntity<ConversationDto> getConversation(
            @PathVariable Integer conversationId,
            @CurrentUser AuthenticatedUser user) {
        return messageService.getConversation(conversationId, user.getUserId())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create conversation", description = "Create a new direct or group conversation")
    @ApiResponse(responseCode = "200", description = "Conversation created")
    @PostMapping("/conversations")
    public ResponseEntity<ConversationDto> createConversation(
            @CurrentUser AuthenticatedUser user,
            @Valid @RequestBody CreateConversationRequest request) {
        
        ConversationDto conversation;
        if ("DIRECT".equalsIgnoreCase(request.getType())) {
            if (request.getParticipantIds().size() != 1) {
                return ResponseEntity.badRequest().build();
            }
            conversation = messageService.createDirectConversation(user.getUserId(), request.getParticipantIds().get(0));
        } else {
            conversation = messageService.createGroupConversation(
                    user.getUserId(), 
                    request.getTitle(), 
                    request.getDescription(), 
                    request.getParticipantIds());
        }
        return ResponseEntity.ok(conversation);
    }

    @Operation(summary = "Get messages", description = "Get messages from a conversation with pagination")
    @ApiResponse(responseCode = "200", description = "Messages retrieved")
    @GetMapping("/conversations/{conversationId}/messages")
    public ResponseEntity<Page<MessageDto>> getMessages(
            @PathVariable Integer conversationId,
            @CurrentUser AuthenticatedUser user,
            @RequestParam(required = false) Integer before,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        
        Page<MessageDto> messages;
        if (before != null) {
            messages = messageService.getMessagesBefore(conversationId, user.getUserId(), before, PageRequest.of(page, size));
        } else {
            messages = messageService.getMessages(conversationId, user.getUserId(), PageRequest.of(page, size));
        }
        return ResponseEntity.ok(messages);
    }

    @Operation(summary = "Send message", description = "Send a message to a conversation (also broadcasts via WebSocket)")
    @ApiResponse(responseCode = "200", description = "Message sent")
    @PostMapping("/send")
    public ResponseEntity<MessageDto> sendMessage(
            @CurrentUser AuthenticatedUser user,
            @Valid @RequestBody SendMessageRequest request) {
        
        MessageDto message = messageService.sendMessage(
                request.getConversationId(),
                user.getUserId(),
                request.getContent(),
                request.getType(),
                request.getUrl());
        return ResponseEntity.ok(message);
    }

    @Operation(summary = "Add member to group", description = "Add a new member to a group conversation (admin only)")
    @PostMapping("/conversations/{conversationId}/members")
    public ResponseEntity<Void> addMember(
            @PathVariable Integer conversationId,
            @CurrentUser AuthenticatedUser user,
            @RequestParam Integer newMemberId) {
        messageService.addMemberToGroup(conversationId, user.getUserId(), newMemberId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Leave conversation", description = "Leave a conversation")
    @DeleteMapping("/conversations/{conversationId}/leave")
    public ResponseEntity<Void> leaveConversation(
            @PathVariable Integer conversationId,
            @CurrentUser AuthenticatedUser user) {
        messageService.leaveConversation(conversationId, user.getUserId());
        return ResponseEntity.ok().build();
    }
}
