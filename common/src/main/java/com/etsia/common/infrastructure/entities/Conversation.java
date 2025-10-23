package com.etsia.common.infrastructure.entities;

import com.etsia.common.domain.model.sub.ConversationRole;
import com.etsia.common.domain.model.sub.ConversationType;
import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "conversations", schema = "public", uniqueConstraints = {
        @UniqueConstraint(name = "conversations_user_one_id_user_two_id_key", columnNames = {"user_one_id", "user_two_id"})
})
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

    @Column(name = "type", columnDefinition = "conversation_type not null")
    @Enumerated(EnumType.STRING)
    private ConversationType type;


    @OneToMany(mappedBy = "conversation")
    private Set<ConversationUser> conversationUsers = new LinkedHashSet<>();

    @OneToMany(mappedBy = "conversation")
    private Set<Message> messages = new LinkedHashSet<>();

}

