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

    @Column(name = "type", nullable = false)
    @Convert(converter = ConversationTypeConverter.class)
    private ConversationType type;


    @Builder.Default
    @OneToMany(mappedBy = "conversation")
    private Set<ConversationUser> conversationUsers = new LinkedHashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "conversation")
    private Set<Message> messages = new LinkedHashSet<>();

}

