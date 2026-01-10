package com.etsia.common.infrastructure.entities;

import com.etsia.common.domain.model.sub.ConversationType;
import com.etsia.common.infrastructure.config.ConversationTypeConverter;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.SQLInsert;
import org.hibernate.annotations.SQLUpdate;
import org.hibernate.type.SqlTypes;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "conversations")
@SQLInsert(sql = "INSERT INTO conversations (description, title, type) VALUES (?, ?, ?::conversation_type)")
@SQLUpdate(sql = "UPDATE conversations SET description = ?, title = ?, type = ?::conversation_type WHERE id = ?")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Conversation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "title", length = Integer.MAX_VALUE)
    private String title;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @Column(name = "type", columnDefinition = "conversation_type NOT NULL")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Convert(converter = ConversationTypeConverter.class)
    private ConversationType type;


    @OneToMany(mappedBy = "conversation")
    private Set<ConversationUser> conversationUsers = new LinkedHashSet<>();

    @OneToMany(mappedBy = "conversation")
    private Set<Message> messages = new LinkedHashSet<>();

}

