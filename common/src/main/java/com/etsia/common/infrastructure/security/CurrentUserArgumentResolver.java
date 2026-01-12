package com.etsia.common.infrastructure.security;

import com.etsia.common.infrastructure.entities.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class CurrentUserArgumentResolver implements HandlerMethodArgumentResolver {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CurrentUser.class) 
                && parameter.getParameterType().equals(AuthenticatedUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, 
                                   ModelAndViewContainer mavContainer,
                                   NativeWebRequest webRequest, 
                                   WebDataBinderFactory binderFactory) {
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            log.debug("No authentication found in security context");
            return null;
        }

        Object principal = authentication.getPrincipal();
        
        if (principal instanceof Jwt jwt) {
            return extractUserFromJwt(jwt);
        }
        
        log.debug("Principal is not a JWT: {}", principal.getClass().getName());
        return null;
    }

    private AuthenticatedUser extractUserFromJwt(Jwt jwt) {
        String keycloakId = jwt.getSubject();
        String email = jwt.getClaimAsString("email");
        String preferredUsername = jwt.getClaimAsString("preferred_username");
        String name = jwt.getClaimAsString("name");
        
        // Extract roles from Keycloak token
        List<String> roles = extractRoles(jwt);
        
        // Get internal user ID from database
        Integer userId = findUserIdByEmail(email);
        
        if (userId == null) {
            // Try by keycloak ID mapping
            userId = findUserIdByKeycloakId(keycloakId);
        }

        return AuthenticatedUser.builder()
                .userId(userId)
                .keycloakId(keycloakId)
                .email(email)
                .username(preferredUsername)
                .name(name)
                .roles(roles)
                .build();
    }

    @SuppressWarnings("unchecked")
    private List<String> extractRoles(Jwt jwt) {
        List<String> roles = new ArrayList<>();
        
        // Try realm_access.roles (Keycloak standard)
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        if (realmAccess != null && realmAccess.containsKey("roles")) {
            Object rolesObj = realmAccess.get("roles");
            if (rolesObj instanceof List) {
                roles.addAll((List<String>) rolesObj);
            }
        }
        
        // Try resource_access for client-specific roles
        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
        if (resourceAccess != null) {
            for (Object clientAccess : resourceAccess.values()) {
                if (clientAccess instanceof Map) {
                    Object clientRoles = ((Map<String, Object>) clientAccess).get("roles");
                    if (clientRoles instanceof List) {
                        roles.addAll((List<String>) clientRoles);
                    }
                }
            }
        }
        
        // Try direct roles claim
        List<String> directRoles = jwt.getClaim("roles");
        if (directRoles != null) {
            roles.addAll(directRoles);
        }
        
        return roles;
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
