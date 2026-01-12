package com.etsia.common.infrastructure.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDislikeId implements Serializable {
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "comment_id", nullable = false)
    private Integer commentId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        CommentDislikeId entity = (CommentDislikeId) o;
        return Objects.equals(this.commentId, entity.commentId) &&
                Objects.equals(this.userId, entity.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commentId, userId);
    }
}
