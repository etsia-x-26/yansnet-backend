package com.etsia.message.infrastructure.repository;

import com.etsia.common.infrastructure.entities.ConversationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("messageConversationUserRepository")
public interface JpaConversationUserRepository extends JpaRepository<ConversationUser, Integer> {
    
    @Query("SELECT cu FROM ConversationUser cu WHERE cu.conversation.id = :conversationId")
    List<ConversationUser> findByConversationId(@Param("conversationId") Integer conversationId);
    
    @Query("SELECT cu FROM ConversationUser cu WHERE cu.conversation.id = :conversationId AND cu.user.id = :userId")
    Optional<ConversationUser> findByConversationIdAndUserId(@Param("conversationId") Integer conversationId, 
                                                              @Param("userId") Integer userId);
    
    @Query("SELECT CASE WHEN COUNT(cu) > 0 THEN true ELSE false END FROM ConversationUser cu " +
           "WHERE cu.conversation.id = :conversationId AND cu.user.id = :userId")
    boolean isUserInConversation(@Param("conversationId") Integer conversationId, @Param("userId") Integer userId);
}
