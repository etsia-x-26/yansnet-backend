package com.etsia.user.application.service;

import com.etsia.common.domain.model.UserDto;
import com.etsia.user.domain.repository.UserRepository;
import com.etsia.user.domain.service.UUserDomainService;
import com.etsia.user.infrastructure.exception.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserFindByEmailAndPasswordService {

    //@Qualifier("uURepository")
    private final UserRepository userRepository;
    private final UUserDomainService UUserDomainService;

    public UserFindByEmailAndPasswordService(UserRepository userRepository, UUserDomainService UUserDomainService) {
        this.userRepository = userRepository;
        this.UUserDomainService = UUserDomainService;
    }

    public Optional<UserDto> exec(String Email, String password){
        if(!UUserDomainService.existsByEmail(Email)){
            throw new UserNotFoundException("User not found");
        }

        return userRepository.FindByEmailAndPassword(Email, password);
    }
}
