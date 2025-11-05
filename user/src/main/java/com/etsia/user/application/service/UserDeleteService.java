package com.etsia.user.application.service;

import com.etsia.common.infrastructure.entities.User;
import com.etsia.user.domain.repository.UserRepository;
import com.etsia.user.domain.service.UUserDomainService;
import com.etsia.user.infrastructure.exception.UserNotFoundException;
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
            throw new UserNotFoundException("User Not Found");
        }
            userRepository.Delete(id);
    }
}
