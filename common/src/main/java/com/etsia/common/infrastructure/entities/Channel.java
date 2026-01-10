package com.etsia.common.infrastructure.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "channels", schema = "public", uniqueConstraints = {
        @UniqueConstraint(name = "channels_title_key", columnNames = {"title"})
})
@AllArgsConstructor
@NamedEntityGraph
@Builder
public class Channel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "title", nullable = false, length = 150)
    private String title;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    public Channel() {

    }

    @ColumnDefault("0")
    @Column(name = "total_followers")
    private int totalFollowers;

    public Channel(String title, String description) {
        this.title = title;
        this.description = description;
    }
}