package com.etsia.message.infrastructure.config;

import com.etsia.message.infrastructure.controller.dto.WebSocketMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {

    private final SimpMessageSendingOperations messagingTemplate;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        log.debug("New WebSocket connection established");
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        
        if (headerAccessor.getSessionAttributes() != null) {
            Integer userId = (Integer) headerAccessor.getSessionAttributes().get("userId");
            Integer conversationId = (Integer) headerAccessor.getSessionAttributes().get("conversationId");
            String username = (String) headerAccessor.getSessionAttributes().get("username");

            if (userId != null && conversationId != null) {
                log.debug("User {} disconnected from conversation {}", userId, conversationId);

                WebSocketMessage message = WebSocketMessage.builder()
                        .action("USER_LEFT")
                        .conversationId(conversationId)
                        .senderId(userId)
                        .senderName(username)
                        .build();

                messagingTemplate.convertAndSend("/topic/conversation/" + conversationId, message);
            }
        }
    }
}
