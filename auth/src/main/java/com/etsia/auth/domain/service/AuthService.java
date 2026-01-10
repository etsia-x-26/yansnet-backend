package com.etsia.auth.domain.service;

import com.etsia.auth.domain.model.AuthUser;
import com.etsia.common.domain.model.sub.Email;
import com.etsia.common.domain.model.sub.PhoneNumber;

public interface AuthService {
    AuthUser authenticate(Email email, String password);

    AuthUser register(Email email, String name, String username, String password, PhoneNumber phoneNumber);

    void logout(Integer userId);

    boolean isValidCredentials(Email email, String password);

    void changePassword(Integer userId, String oldPassword, String newPassword);
}