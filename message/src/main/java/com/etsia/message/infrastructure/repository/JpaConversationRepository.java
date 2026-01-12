package com.etsia.message.infrastructure.repository;

import com.etsia.common.infrastructure.entities.Conversation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("messageConversationRepository")
public interface JpaConversationRepository extends JpaRepository<Conversation, Integer> {
    
    @Query("SELECT DISTINCT c FROM Conversation c " +
           "JOIN c.conversationUsers cu " +
           "WHERE cu.user.id = :userId " +
           "ORDER BY c.id DESC")
    Page<Conversation> findByUserId(@Param("userId") Integer userId, Pageable pageable);
    
    @Query("SELECT c FROM Conversation c " +
           "JOIN c.conversationUsers cu1 " +
           "JOIN c.conversationUsers cu2 " +
           "WHERE cu1.user.id = :userId1 AND cu2.user.id = :userId2 " +
           "AND c.type = 'PRIVATE'")
    Optional<Conversation> findDirectConversation(@Param("userId1") Integer userId1, 
                                                   @Param("userId2") Integer userId2);
}
