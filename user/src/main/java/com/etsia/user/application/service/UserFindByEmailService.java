package com.etsia.user.application.service;

import com.etsia.common.domain.model.UserDto;
import com.etsia.common.domain.model.sub.Email;

import com.etsia.common.infrastructure.entities.User;
import com.etsia.user.domain.repository.UserRepository;
import com.etsia.user.domain.service.UserDomainService;
import com.etsia.user.infrastructure.exception.EmailNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserFindByEmailService {
    private final UserRepository userRepository;
    private final UserDomainService userDomainService;

    public Optional<UserDto> exec(String Email){
        if(!userDomainService.existsByEmail(Email)){
            throw new EmailNotFoundException("Email Not found");
        }

        return userRepository.FindByEmail(Email);
    }
}
