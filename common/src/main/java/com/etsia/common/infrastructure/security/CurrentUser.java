package com.etsia.common.infrastructure.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to inject the current authenticated user into controller methods.
 * 
 * Usage:
 * @GetMapping("/profile")
 * public ResponseEntity<UserDto> getProfile(@CurrentUser AuthenticatedUser user) {
 *     // user.getUserId(), user.getEmail(), etc.
 * }
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface CurrentUser {
}
