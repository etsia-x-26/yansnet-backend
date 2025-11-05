package com.etsia.user.application.service;

import com.etsia.user.domain.repository.UserRepository;
import com.etsia.user.domain.service.UUserDomainService;
import org.springframework.stereotype.Service;

@Service
public class UserDeleteService {

    //@Qualifier("uURepository")
    private final UserRepository userRepository;
    private final UUserDomainService UUserDomainService;
    //private final UserDomainService userDomainService;

    public UserDeleteService(UserRepository userRepository, UUserDomainService uUserDomainService) {
        this.userRepository = userRepository;
        UUserDomainService = uUserDomainService;
    }

    public void execute(Integer id){
        if(!UUserDomainService.FindById(id).isPresent()){
            throw new IllegalArgumentException("User Not Found");
        }
            userRepository.Delete(id);
    }
}
