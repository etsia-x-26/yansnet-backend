package com.etsia.common.infrastructure.entities;

import com.etsia.common.domain.model.sub.ConversationRole;
import com.etsia.common.infrastructure.config.ConversationRoleConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.SQLInsert;
import org.hibernate.annotations.SQLUpdate;
import org.hibernate.type.SqlTypes;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "conversation_users")
@SQLInsert(sql = "INSERT INTO conversation_users (conversation_id, role, user_id) VALUES (?, ?::conversation_role, ?)")
@SQLUpdate(sql = "UPDATE conversation_users SET conversation_id = ?, role = ?::conversation_role, user_id = ? WHERE id = ?")
public class ConversationUser {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "conversation_id", nullable = false)
    private Conversation conversation;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "role", columnDefinition = "conversation_role not null")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Convert(converter = ConversationRoleConverter.class)
    private ConversationRole role;
}