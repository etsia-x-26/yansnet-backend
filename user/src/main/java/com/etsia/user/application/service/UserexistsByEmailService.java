package com.etsia.user.application.service;

import com.etsia.user.domain.repository.UserRepository;
import com.etsia.user.domain.service.UUserDomainService;
import org.springframework.stereotype.Service;

@Service
public class UserexistsByEmailService {

    //@Qualifier("uURepository")
    private final UserRepository userRepository;
    //@Qualifier("uUDomainService")
    private final UUserDomainService UUserDomainService;

    public UserexistsByEmailService(UserRepository userRepository, UUserDomainService UUserDomainService) {
        this.userRepository = userRepository;
        this.UUserDomainService = UUserDomainService;
    }

    public Boolean exec(String Email){
        //if(!UUserDomainService.existsByEmail(Email)){
        //    throw new IllegalArgumentException("Email Not found");
        //}

        return userRepository.existsByEmail(Email);
    }
}
