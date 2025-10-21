package com.etsia.common.infrastructure.entities;

import com.etsia.common.domain.model.sub.MediaType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "media", schema = "public", uniqueConstraints = {
        @UniqueConstraint(name = "media_post_id_url_key", columnNames = {"post_id", "url"})
})
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Media {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "url", nullable = false)
    private String url;

    @ColumnDefault("now()")
    @Column(name = "uploaded_at")
    private Instant uploadedAt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", columnDefinition = "media_type not null")
    private MediaType type;

}



