package com.etsia.common.infrastructure.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "posts", schema = "public", indexes = {
        @Index(name = "idx_posts_user_id", columnList = "user_id"),
        @Index(name = "idx_posts_channel_id", columnList = "channel_id")
})
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ColumnDefault("nextval('posts_id_seq')")
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "content", nullable = false, length = Integer.MAX_VALUE)
    private String content;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    @ColumnDefault("now()")
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id")
    private Channel channel;

    @ColumnDefault("0")
    @Column(name = "total_likes", columnDefinition = "positive_int")
    private int totalLikes;
    @ColumnDefault("0")
    @Column(name = "total_comments", columnDefinition = "positive_int")
    private int totalComments;

    @OneToMany(mappedBy = "post",cascade = CascadeType.ALL , orphanRemoval = true)
    private Set<Media> media = new LinkedHashSet<>();

}