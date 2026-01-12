package com.etsia.common.infrastructure.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "comment_dislikes", schema = "public")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDislike {
    @EmbeddedId
    private CommentDislikeId id;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @MapsId("commentId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;
}
