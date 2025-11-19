package com.etsia.common.infrastructure.entities;

import com.etsia.common.domain.model.sub.GroupRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "group_members", schema = "public")
@AllArgsConstructor
@Builder
public class GroupMember {
    @EmbeddedId
    private GroupMemberId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("groupId")
    @JoinColumn(name = "group_id")
    private Group group;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", columnDefinition = "group_role")
    @ColumnDefault("'MEMBER'")
    private GroupRole role;

    @ColumnDefault("NOW()")
    @Column(name = "joined_at")
    private LocalDateTime joinedAt;

    public GroupMember() {
    }

    @PrePersist
    protected void onCreate() {
        joinedAt = LocalDateTime.now();
        if (role == null) {
            role = GroupRole.MEMBER;
        }
    }
}
