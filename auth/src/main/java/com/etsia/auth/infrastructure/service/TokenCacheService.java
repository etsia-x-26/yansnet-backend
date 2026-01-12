package com.etsia.auth.infrastructure.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenCacheService {

    private final RedisTemplate<String, Object> redisTemplate;
    
    private static final String REFRESH_TOKEN_PREFIX = "refresh_token:";
    private static final String USER_TOKEN_PREFIX = "user_tokens:";
    private static final String BLACKLIST_PREFIX = "blacklist:";

    public void storeRefreshToken(String refreshToken, Integer userId, long expirationMs) {
        String key = REFRESH_TOKEN_PREFIX + refreshToken;
        redisTemplate.opsForValue().set(key, userId, expirationMs, TimeUnit.MILLISECONDS);
        
        // Store user -> token mapping for logout
        String userKey = USER_TOKEN_PREFIX + userId;
        redisTemplate.opsForSet().add(userKey, refreshToken);
        redisTemplate.expire(userKey, expirationMs, TimeUnit.MILLISECONDS);
        
        log.debug("Stored refresh token for user {}", userId);
    }

    public Integer getUserIdFromRefreshToken(String refreshToken) {
        String key = REFRESH_TOKEN_PREFIX + refreshToken;
        Object userId = redisTemplate.opsForValue().get(key);
        return userId != null ? (Integer) userId : null;
    }

    public void invalidateRefreshToken(String refreshToken) {
        String key = REFRESH_TOKEN_PREFIX + refreshToken;
        Integer userId = getUserIdFromRefreshToken(refreshToken);
        
        redisTemplate.delete(key);
        
        if (userId != null) {
            String userKey = USER_TOKEN_PREFIX + userId;
            redisTemplate.opsForSet().remove(userKey, refreshToken);
        }
        
        log.debug("Invalidated refresh token");
    }

    public void invalidateAllUserTokens(Integer userId) {
        String userKey = USER_TOKEN_PREFIX + userId;
        var tokens = redisTemplate.opsForSet().members(userKey);
        
        if (tokens != null) {
            for (Object token : tokens) {
                String tokenKey = REFRESH_TOKEN_PREFIX + token;
                redisTemplate.delete(tokenKey);
            }
        }
        
        redisTemplate.delete(userKey);
        log.debug("Invalidated all tokens for user {}", userId);
    }

    public void blacklistAccessToken(String accessToken, long expirationMs) {
        String key = BLACKLIST_PREFIX + accessToken;
        redisTemplate.opsForValue().set(key, "blacklisted", expirationMs, TimeUnit.MILLISECONDS);
    }

    public boolean isAccessTokenBlacklisted(String accessToken) {
        String key = BLACKLIST_PREFIX + accessToken;
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
}
