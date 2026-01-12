package com.etsia.common.infrastructure.security;

import com.etsia.common.infrastructure.entities.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * RequestBodyAdvice that automatically injects the current user's ID into DTOs
 * that implement the {@link UserIdAware} interface.
 * 
 * This eliminates the need to manually extract the user from the JWT token
 * and set the userId in each controller method.
 * 
 * Usage:
 * 1. Have your DTO implement UserIdAware
 * 2. The userId will be automatically injected before the controller method is called
 * 
 * Example controller (no manual user injection needed):
 * @PostMapping
 * public ResponseEntity<PostDto> createPost(@RequestBody CreatePostRequest request) {
 *     // request.getUserId() is already set from the JWT token
 *     return ResponseEntity.ok(postService.save(request));
 * }
 */
@ControllerAdvice
@Slf4j
public class UserIdInjectorAdvice implements RequestBodyAdvice {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType,
                           Class<? extends HttpMessageConverter<?>> converterType) {
        // Only process if the target type implements UserIdAware
        if (targetType instanceof Class<?> clazz) {
            return UserIdAware.class.isAssignableFrom(clazz);
        }
        return false;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter,
                                           Type targetType, Class<? extends HttpMessageConverter<?>> converterType)
            throws IOException {
        // No modification before reading
        return inputMessage;
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter,
                                Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        
        if (body instanceof UserIdAware userIdAware) {
            Integer userId = getCurrentUserId();
            if (userId != null) {
                userIdAware.setUserId(userId);
                log.debug("Injected userId {} into {}", userId, body.getClass().getSimpleName());
            } else {
                log.warn("Could not resolve userId for authenticated user, DTO: {}", body.getClass().getSimpleName());
            }
        }
        
        return body;
    }

    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter,
                                  Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        // Return body as-is for empty body cases
        return body;
    }

    /**
     * Extracts the current user's internal ID from the JWT token in the security context.
     * 
     * @return the internal user ID, or null if not authenticated or user not found
     */
    private Integer getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            log.debug("No authentication found in security context");
            return null;
        }

        Object principal = authentication.getPrincipal();
        
        if (principal instanceof Jwt jwt) {
            return extractUserIdFromJwt(jwt);
        }
        
        log.debug("Principal is not a JWT: {}", principal.getClass().getName());
        return null;
    }

    /**
     * Extracts the internal user ID from a JWT token.
     * First tries to find the user by email, then by keycloak ID.
     */
    private Integer extractUserIdFromJwt(Jwt jwt) {
        String keycloakId = jwt.getSubject();
        String email = jwt.getClaimAsString("email");
        
        // Try to find user by email first
        Integer userId = findUserIdByEmail(email);
        
        if (userId == null) {
            // Try by keycloak ID mapping
            userId = findUserIdByKeycloakId(keycloakId);
        }
        
        return userId;
    }

    private Integer findUserIdByEmail(String email) {
        if (email == null) return null;
        
        try {
            User user = entityManager.createQuery(
                    "SELECT u FROM User u WHERE u.email = :email", User.class)
                    .setParameter("email", new com.etsia.common.domain.model.sub.Email(email))
                    .getSingleResult();
            return user.getId();
        } catch (NoResultException e) {
            log.debug("No user found with email: {}", email);
            return null;
        } catch (Exception e) {
            log.error("Error finding user by email: {}", e.getMessage());
            return null;
        }
    }

    private Integer findUserIdByKeycloakId(String keycloakId) {
        if (keycloakId == null) return null;
        
        try {
            Integer userId = entityManager.createQuery(
                    "SELECT k.id FROM KeycloakUserMapping k WHERE k.keycloakUserId = :keycloakId", Integer.class)
                    .setParameter("keycloakId", keycloakId)
                    .getSingleResult();
            return userId;
        } catch (NoResultException e) {
            log.debug("No user mapping found for keycloak ID: {}", keycloakId);
            return null;
        } catch (Exception e) {
            log.error("Error finding user by keycloak ID: {}", e.getMessage());
            return null;
        }
    }
}
