package com.etsia.common.infrastructure.entities;

import com.etsia.common.domain.model.sub.Email;
import com.etsia.common.domain.model.sub.PhoneNumber;
import com.etsia.common.infrastructure.config.EmailConverter;
import com.etsia.common.infrastructure.config.PhoneNumberConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "users", schema = "public", indexes = {
        @Index(name = "users_email_key", columnList = "email", unique = true)
})
@AllArgsConstructor
@NamedEntityGraph
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "password", nullable = false)
    private String password;

    @ColumnDefault("true")
    @Column(name = "is_active")
    private Boolean isActive;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private UserCategory category;

    @ColumnDefault("false")
    @Column(name = "is_blocked")
    private Boolean isBlocked;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batch_id")
    private Batch batch;

    @Column(name = "email", nullable = false, unique = true)
    @Convert(converter = EmailConverter.class)
    private Email email;

    @Column(name = "phone_number")
    @Convert(converter = PhoneNumberConverter.class)
    private PhoneNumber phoneNumber;

    @ColumnDefault("0")
    @Column(name = "total_followers")
    private int totalFollowers;

    @ColumnDefault("0")
    @Column(name = "total_following")
    private int totalFollowing;
    @ColumnDefault("0")
    @Column(name = "total_posts")
    private int totalPosts;

    @Size(max = 50)
    @NotNull
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Size(max = 255)
    @NotNull
    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "bio", length = Integer.MAX_VALUE)
    private String bio;

    @Column(name = "profile_picture_url", length = Integer.MAX_VALUE)
    private String profilePictureUrl;

    @ColumnDefault("false")
    @Column(name = "is_mentor")
    private Boolean isMentor;

    @Column(name = "last_login")
    private Instant lastLogin;

    @Column(name = "promotion_year")
    private Integer promotionYear;

    public User() {

    }
}

