package com.etsia.interaction.infrastructure.repository.conversation;

import com.etsia.common.domain.model.sub.ConversationRole;
import com.etsia.common.infrastructure.entities.ConversationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaConversationUserRepository extends JpaRepository<ConversationUser, Integer> {

    @Query("select exists(select 1 from ConversationUser where conversation.id = :conversationId and user.id = :userId)")
    boolean existsByConversationIdAndUserId(int conversationId, int userId);

    @Query("select cu from ConversationUser cu where cu.conversation.id = :conversationId and cu.user.id = :userId")
    Optional<ConversationUser> findByConversationIdAndUserId(int conversationId, int userId);

    @Query("select cu from ConversationUser cu join fetch cu.conversation join fetch cu.user where cu.id = :id")
    Optional<ConversationUser> findByIdWithRelations(Integer id);

    @Query("select cu from ConversationUser cu join fetch cu.conversation join fetch cu.user where cu.conversation.id = :conversationId")
    List<ConversationUser> findByConversationIdWithRelations(Integer conversationId);

    @Query("select cu from ConversationUser cu join fetch cu.conversation join fetch cu.user where cu.user.id = :userId")
    List<ConversationUser> findByUserIdWithRelations(Integer userId);

    @Query("select cu from ConversationUser cu join fetch cu.conversation join fetch cu.user where cu.conversation.id = :conversationId and cu.role = :role")
    List<ConversationUser> findByConversationIdAndRoleWithRelations(Integer conversationId, ConversationRole role);
}
