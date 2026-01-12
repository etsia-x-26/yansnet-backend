package com.etsia.common.infrastructure.security;

/**
 * Interface for DTOs that need the current user's ID injected automatically from the JWT token.
 * 
 * Usage:
 * 1. Implement this interface in your DTO
 * 2. The userId will be automatically injected from the JWT token before the controller method is called
 * 
 * Example:
 * public class CreatePostRequest implements UserIdAware {
 *     private String content;
 *     private Integer userId;
 *     
 *     @Override
 *     public void setUserId(Integer userId) { this.userId = userId; }
 *     
 *     @Override
 *     public Integer getUserId() { return userId; }
 * }
 */
public interface UserIdAware {
    
    /**
     * Sets the user ID from the authenticated JWT token.
     * This method is called automatically by UserIdInjectorAdvice.
     * 
     * @param userId the internal user ID
     */
    void setUserId(Integer userId);
    
    /**
     * Gets the user ID.
     * 
     * @return the user ID
     */
    Integer getUserId();
}
