package com.etsia.common.infrastructure.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "keycloak_user_mapping")
public class KeycloakUserMapping {
    @Id
    @Column(name = "internal_user_id", nullable = false)
    private Integer id;

    @Column(name = "created_at")
    private Instant createdAt;

    @Size(max = 36)
    @NotNull
    @Column(name = "keycloak_user_id", nullable = false, length = 36)
    private String keycloakUserId;

}