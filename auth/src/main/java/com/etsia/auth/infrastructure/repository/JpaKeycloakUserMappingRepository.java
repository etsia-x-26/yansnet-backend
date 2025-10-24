package com.etsia.auth.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaKeycloakUserMappingRepository extends JpaRepository<KeycloakUserMappingEntity, Integer> {

    Optional<KeycloakUserMappingEntity> findByKeycloakUserId(String keycloakUserId);

    Optional<KeycloakUserMappingEntity> findByInternalUserId(Integer internalUserId);

    boolean existsByKeycloakUserId(String keycloakUserId);

    boolean existsByInternalUserId(Integer internalUserId);

    @Query(value = "SELECT nextval('user_id_seq')", nativeQuery = true)
    Integer getNextUserId();
}