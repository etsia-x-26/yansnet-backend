package com.etsia.message.infrastructure.repository;

import com.etsia.common.infrastructure.entities.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("messageMessageRepository")
public interface JpaMessageRepository extends JpaRepository<Message, Integer> {
    
    @Query("SELECT m FROM Message m WHERE m.conversation.id = :conversationId ORDER BY m.id DESC")
    Page<Message> findByConversationId(@Param("conversationId") Integer conversationId, Pageable pageable);
    
    @Query("SELECT m FROM Message m WHERE m.conversation.id = :conversationId ORDER BY m.id DESC")
    List<Message> findRecentByConversationId(@Param("conversationId") Integer conversationId, Pageable pageable);
    
    @Query("SELECT m FROM Message m WHERE m.conversation.id = :conversationId AND m.id < :beforeId ORDER BY m.id DESC")
    Page<Message> findByConversationIdBefore(@Param("conversationId") Integer conversationId, 
                                              @Param("beforeId") Integer beforeId, 
                                              Pageable pageable);
}
