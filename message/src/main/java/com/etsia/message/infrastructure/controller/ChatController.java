package com.etsia.message.infrastructure.controller;

import com.etsia.message.domain.model.MessageDto;
import com.etsia.message.domain.service.MessageService;
import com.etsia.message.infrastructure.controller.dto.WebSocketMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessageService messageService;

    /**
     * Send a message to a conversation
     * Client sends to: /app/chat/{conversationId}/send
     * Broadcasts to: /topic/conversation/{conversationId}
     */
    @MessageMapping("/chat/{conversationId}/send")
    public void sendMessage(@DestinationVariable Integer conversationId,
                            @Payload WebSocketMessage wsMessage,
                            SimpMessageHeaderAccessor headerAccessor) {
        log.debug("Received message for conversation {}: {}", conversationId, wsMessage);
        
        try {
            // Save message to database
            MessageDto savedMessage = messageService.sendMessage(
                    conversationId,
                    wsMessage.getSenderId(),
                    wsMessage.getMessage() != null ? wsMessage.getMessage().getContent() : null,
                    wsMessage.getMessage() != null ? wsMessage.getMessage().getType() : "TEXT",
                    wsMessage.getMessage() != null ? wsMessage.getMessage().getUrl() : null
            );

            // Broadcast to all participants
            WebSocketMessage response = WebSocketMessage.builder()
                    .action("MESSAGE")
                    .conversationId(conversationId)
                    .senderId(wsMessage.getSenderId())
                    .senderName(wsMessage.getSenderName())
                    .message(savedMessage)
                    .build();

            messagingTemplate.convertAndSend("/topic/conversation/" + conversationId, response);
            log.debug("Message broadcasted to /topic/conversation/{}", conversationId);
            
        } catch (Exception e) {
            log.error("Error sending message: {}", e.getMessage());
        }
    }

    /**
     * Typing indicator
     * Client sends to: /app/chat/{conversationId}/typing
     * Broadcasts to: /topic/conversation/{conversationId}
     */
    @MessageMapping("/chat/{conversationId}/typing")
    public void typing(@DestinationVariable Integer conversationId,
                       @Payload WebSocketMessage wsMessage) {
        WebSocketMessage response = WebSocketMessage.builder()
                .action("TYPING")
                .conversationId(conversationId)
                .senderId(wsMessage.getSenderId())
                .senderName(wsMessage.getSenderName())
                .build();

        messagingTemplate.convertAndSend("/topic/conversation/" + conversationId, response);
    }

    /**
     * Mark messages as read
     * Client sends to: /app/chat/{conversationId}/read
     * Broadcasts to: /topic/conversation/{conversationId}
     */
    @MessageMapping("/chat/{conversationId}/read")
    public void markAsRead(@DestinationVariable Integer conversationId,
                           @Payload WebSocketMessage wsMessage) {
        WebSocketMessage response = WebSocketMessage.builder()
                .action("READ")
                .conversationId(conversationId)
                .senderId(wsMessage.getSenderId())
                .payload(wsMessage.getPayload()) // Contains messageId or lastReadMessageId
                .build();

        messagingTemplate.convertAndSend("/topic/conversation/" + conversationId, response);
    }

    /**
     * User joins a conversation (subscribes)
     * Client sends to: /app/chat/{conversationId}/join
     * Broadcasts to: /topic/conversation/{conversationId}
     */
    @MessageMapping("/chat/{conversationId}/join")
    public void joinConversation(@DestinationVariable Integer conversationId,
                                 @Payload WebSocketMessage wsMessage,
                                 SimpMessageHeaderAccessor headerAccessor) {
        // Store user info in session
        if (headerAccessor.getSessionAttributes() != null) {
            headerAccessor.getSessionAttributes().put("userId", wsMessage.getSenderId());
            headerAccessor.getSessionAttributes().put("conversationId", conversationId);
        }

        WebSocketMessage response = WebSocketMessage.builder()
                .action("USER_JOINED")
                .conversationId(conversationId)
                .senderId(wsMessage.getSenderId())
                .senderName(wsMessage.getSenderName())
                .build();

        messagingTemplate.convertAndSend("/topic/conversation/" + conversationId, response);
    }

    /**
     * Send direct message to a specific user
     * Client sends to: /app/user/{userId}/message
     * Delivers to: /user/{userId}/queue/messages
     */
    @MessageMapping("/user/{userId}/message")
    public void sendDirectMessage(@DestinationVariable Integer userId,
                                  @Payload WebSocketMessage wsMessage) {
        messagingTemplate.convertAndSendToUser(
                userId.toString(),
                "/queue/messages",
                wsMessage
        );
    }
}
