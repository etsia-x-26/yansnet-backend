package com.etsia.message.domain.service;

import com.etsia.common.domain.model.sub.ConversationRole;
import com.etsia.common.domain.model.sub.ConversationType;
import com.etsia.common.domain.model.sub.MessageType;
import com.etsia.common.infrastructure.entities.Conversation;
import com.etsia.common.infrastructure.entities.ConversationUser;
import com.etsia.common.infrastructure.entities.Message;
import com.etsia.common.infrastructure.entities.User;
import com.etsia.message.domain.model.ConversationDto;
import com.etsia.message.domain.model.MessageDto;
import com.etsia.message.domain.model.ParticipantDto;
import com.etsia.message.infrastructure.repository.JpaConversationRepository;
import com.etsia.message.infrastructure.repository.JpaConversationUserRepository;
import com.etsia.message.infrastructure.repository.JpaMessageRepository;
import com.etsia.message.infrastructure.repository.JpaUserMessageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MessageService {

    private final JpaMessageRepository messageRepository;
    private final JpaConversationRepository conversationRepository;
    private final JpaConversationUserRepository conversationUserRepository;
    private final JpaUserMessageRepository userRepository;

    public MessageService(
            @Qualifier("messageMessageRepository") JpaMessageRepository messageRepository,
            @Qualifier("messageConversationRepository") JpaConversationRepository conversationRepository,
            @Qualifier("messageConversationUserRepository") JpaConversationUserRepository conversationUserRepository,
            @Qualifier("messageUserRepository") JpaUserMessageRepository userRepository) {
        this.messageRepository = messageRepository;
        this.conversationRepository = conversationRepository;
        this.conversationUserRepository = conversationUserRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public Page<ConversationDto> getUserConversations(Integer userId, Pageable pageable) {
        return conversationRepository.findByUserId(userId, pageable)
                .map(this::mapToConversationDto);
    }

    @Transactional(readOnly = true)
    public Optional<ConversationDto> getConversation(Integer conversationId, Integer userId) {
        if (!conversationUserRepository.isUserInConversation(conversationId, userId)) {
            return Optional.empty();
        }
        return conversationRepository.findById(conversationId)
                .map(this::mapToConversationDto);
    }

    @Transactional(readOnly = true)
    public Page<MessageDto> getMessages(Integer conversationId, Integer userId, Pageable pageable) {
        if (!conversationUserRepository.isUserInConversation(conversationId, userId)) {
            throw new IllegalArgumentException("User is not a participant of this conversation");
        }
        return messageRepository.findByConversationId(conversationId, pageable)
                .map(this::mapToMessageDto);
    }

    @Transactional(readOnly = true)
    public Page<MessageDto> getMessagesBefore(Integer conversationId, Integer userId, Integer beforeId, Pageable pageable) {
        if (!conversationUserRepository.isUserInConversation(conversationId, userId)) {
            throw new IllegalArgumentException("User is not a participant of this conversation");
        }
        return messageRepository.findByConversationIdBefore(conversationId, beforeId, pageable)
                .map(this::mapToMessageDto);
    }

    @Transactional
    public ConversationDto createDirectConversation(Integer userId1, Integer userId2) {
        // Check if conversation already exists
        Optional<Conversation> existing = conversationRepository.findDirectConversation(userId1, userId2);
        if (existing.isPresent()) {
            return mapToConversationDto(existing.get());
        }

        User user1 = userRepository.findById(userId1)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId1));
        User user2 = userRepository.findById(userId2)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId2));

        Conversation conversation = Conversation.builder()
                .type(ConversationType.PRIVATE)
                .build();
        conversation = conversationRepository.save(conversation);

        ConversationUser cu1 = ConversationUser.builder()
                .conversation(conversation)
                .user(user1)
                .role(ConversationRole.USER)
                .build();
        ConversationUser cu2 = ConversationUser.builder()
                .conversation(conversation)
                .user(user2)
                .role(ConversationRole.USER)
                .build();

        conversationUserRepository.save(cu1);
        conversationUserRepository.save(cu2);

        return mapToConversationDto(conversation);
    }

    @Transactional
    public ConversationDto createGroupConversation(Integer creatorId, String title, String description, List<Integer> memberIds) {
        User creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new IllegalArgumentException("Creator not found"));

        Conversation conversation = Conversation.builder()
                .title(title)
                .description(description)
                .type(ConversationType.PUBLIC)
                .build();
        conversation = conversationRepository.save(conversation);

        // Add creator as admin
        ConversationUser creatorMember = ConversationUser.builder()
                .conversation(conversation)
                .user(creator)
                .role(ConversationRole.ADMIN)
                .build();
        conversationUserRepository.save(creatorMember);

        // Add other members
        for (Integer memberId : memberIds) {
            if (!memberId.equals(creatorId)) {
                User member = userRepository.findById(memberId).orElse(null);
                if (member != null) {
                    ConversationUser cu = ConversationUser.builder()
                            .conversation(conversation)
                            .user(member)
                            .role(ConversationRole.USER)
                            .build();
                    conversationUserRepository.save(cu);
                }
            }
        }

        return mapToConversationDto(conversation);
    }

    @Transactional
    public MessageDto sendMessage(Integer conversationId, Integer senderId, String content, String type, String url) {
        if (!conversationUserRepository.isUserInConversation(conversationId, senderId)) {
            throw new IllegalArgumentException("User is not a participant of this conversation");
        }

        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new IllegalArgumentException("Conversation not found"));
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new IllegalArgumentException("Sender not found"));

        Message message = new Message();
        message.setConversation(conversation);
        message.setUser(sender);
        message.setContent(content);
        message.setUrl(url);
        message.setType(MessageType.valueOf(type != null ? type : "TEXT"));

        message = messageRepository.save(message);
        log.debug("Message saved: {} in conversation {}", message.getId(), conversationId);

        return mapToMessageDto(message);
    }

    @Transactional
    public void addMemberToGroup(Integer conversationId, Integer adminId, Integer newMemberId) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new IllegalArgumentException("Conversation not found"));

        if (conversation.getType() != ConversationType.PUBLIC) {
            throw new IllegalArgumentException("Cannot add members to direct conversation");
        }

        ConversationUser admin = conversationUserRepository.findByConversationIdAndUserId(conversationId, adminId)
                .orElseThrow(() -> new IllegalArgumentException("Admin not found in conversation"));

        if (admin.getRole() != ConversationRole.ADMIN) {
            throw new IllegalArgumentException("Only admins can add members");
        }

        if (conversationUserRepository.isUserInConversation(conversationId, newMemberId)) {
            throw new IllegalArgumentException("User is already a member");
        }

        User newMember = userRepository.findById(newMemberId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        ConversationUser cu = ConversationUser.builder()
                .conversation(conversation)
                .user(newMember)
                .role(ConversationRole.USER)
                .build();
        conversationUserRepository.save(cu);
    }

    @Transactional
    public void leaveConversation(Integer conversationId, Integer userId) {
        ConversationUser cu = conversationUserRepository.findByConversationIdAndUserId(conversationId, userId)
                .orElseThrow(() -> new IllegalArgumentException("User not in conversation"));
        conversationUserRepository.delete(cu);
    }

    // Mappers
    private ConversationDto mapToConversationDto(Conversation conversation) {
        List<ParticipantDto> participants = conversationUserRepository.findByConversationId(conversation.getId())
                .stream()
                .map(this::mapToParticipantDto)
                .collect(Collectors.toList());

        List<Message> recentMessages = messageRepository.findRecentByConversationId(
                conversation.getId(), PageRequest.of(0, 1));
        MessageDto lastMessage = recentMessages.isEmpty() ? null : mapToMessageDto(recentMessages.get(0));

        return ConversationDto.builder()
                .id(conversation.getId())
                .title(conversation.getTitle())
                .description(conversation.getDescription())
                .type(conversation.getType() != null ? conversation.getType().name() : null)
                .participants(participants)
                .lastMessage(lastMessage)
                .build();
    }

    private ParticipantDto mapToParticipantDto(ConversationUser cu) {
        User user = cu.getUser();
        return ParticipantDto.builder()
                .userId(user.getId())
                .name(user.getName())
                .username(user.getUsername())
                .avatarUrl(user.getProfilePictureUrl())
                .role(cu.getRole() != null ? cu.getRole().name() : null)
                .build();
    }

    private MessageDto mapToMessageDto(Message message) {
        User sender = message.getUser();
        return MessageDto.builder()
                .id(message.getId())
                .conversationId(message.getConversation().getId())
                .senderId(sender != null ? sender.getId() : null)
                .senderName(sender != null ? sender.getName() : null)
                .senderAvatar(sender != null ? sender.getProfilePictureUrl() : null)
                .content(message.getContent())
                .url(message.getUrl())
                .type(message.getType() != null ? message.getType().name() : "TEXT")
                .build();
    }
}
