package com.etsia.user.application.service;

import com.etsia.common.domain.model.UserDto;
import com.etsia.user.domain.repository.UserRepository;
import com.etsia.user.domain.service.UUserDomainService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserFindByEmailService {
    private final UserRepository userRepository;
    private final UUserDomainService UUserDomainService;

    public Optional<UserDto> exec(String Email){
        if(!UUserDomainService.existsByEmail(Email)){
            throw new IllegalArgumentException("Email Not found");
        }

        return userRepository.FindByEmail(Email);
    }
}
