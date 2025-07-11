package com.etsia.common.infrastructure.entities;

import com.etsia.common.domain.model.sub.Email;
import com.etsia.common.domain.model.sub.PhoneNumber;
import com.etsia.common.infrastructure.config.EmailConverter;
import com.etsia.common.infrastructure.config.PhoneNumberConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

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

    @Column(name = "email", columnDefinition = "email_type", nullable = false)
    @Convert(converter = EmailConverter.class)
    private Email email;

    @Column(name = "phone_number", columnDefinition = "phone_type")
    @Convert(converter = PhoneNumberConverter.class)
    private PhoneNumber phoneNumber;

    @ColumnDefault("0")
    @Column(name = "total_followers", columnDefinition = "positive_int")
    private int totalFollowers;

    @ColumnDefault("0")
    @Column(name = "total_following", columnDefinition = "positive_int")
    private int totalFollowing;
    @ColumnDefault("0")
    @Column(name = "total_posts", columnDefinition = "positive_int")
    private int totalPosts;

    public User() {

    }
}

