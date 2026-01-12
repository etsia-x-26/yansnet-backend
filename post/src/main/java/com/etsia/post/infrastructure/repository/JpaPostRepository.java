package com.etsia.post.infrastructure.repository;

import com.etsia.common.infrastructure.entities.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaPostRepository extends JpaRepository<Post, Integer> {
    Page<Post> findAll(Pageable pageable);
    
    @Query("SELECT p FROM Post p WHERE p.id = :id AND p.deletedAt IS NULL")
    Optional<Post> findByIdAndNotDeleted(@Param("id") Integer id);
    
    @Query("SELECT p FROM Post p WHERE p.user.id = :userId AND p.deletedAt IS NULL ORDER BY p.createdAt DESC")
    Page<Post> findByUserId(@Param("userId") Integer userId, Pageable pageable);
    
    @Query("SELECT p FROM Post p WHERE LOWER(p.content) LIKE LOWER(CONCAT('%', :query, '%')) AND p.deletedAt IS NULL")
    Page<Post> searchByContent(@Param("query") String query, Pageable pageable);
}
