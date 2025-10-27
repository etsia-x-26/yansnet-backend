package com.etsia.user.application.service;

import com.etsia.user.domain.repository.UserRepository;
import com.etsia.user.domain.service.UUserDomainService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserexistsByEmailService {

    private final UserRepository userRepository;
    private final UUserDomainService UUserDomainService;

    public Boolean exec(String Email){
        if(!UUserDomainService.existsByEmail(Email)){
            throw new IllegalArgumentException("Email Not found");
        }

        return userRepository.existsByEmail(Email);
    }
}
