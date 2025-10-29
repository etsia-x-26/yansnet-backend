package com.etsia.user.application.service;

import com.etsia.common.domain.model.UserDto;
import com.etsia.user.domain.repository.UserRepository;
import com.etsia.user.domain.service.UUserDomainService;
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

        return userRepository.FindByEmail(Email);
    }
}
