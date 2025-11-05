package com.etsia.user.application.service;

import com.etsia.common.domain.model.UserDto;
import com.etsia.user.domain.repository.UserRepository;
import com.etsia.user.domain.service.UUserDomainService;
import com.etsia.user.infrastructure.exception.EmailNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserFindByEmailService {
    //@Qualifier("uURepository")
    private final UserRepository userRepository;
    //@Qualifier("uUDomainService")
    private final UUserDomainService UUserDomainService;

    public UserFindByEmailService(UserRepository userRepository, UUserDomainService UUserDomainService) {
        this.userRepository = userRepository;
        this.UUserDomainService = UUserDomainService;
    }

    public Optional<UserDto> exec(String Email){
        if(!UUserDomainService.existsByEmail(Email)){
            throw new IllegalArgumentException("Email Not found");
        }
        return Optional.ofNullable(userRepository.FindByEmail(Email).orElseThrow(() -> new IllegalArgumentException("Email Not found")));
    }
}
