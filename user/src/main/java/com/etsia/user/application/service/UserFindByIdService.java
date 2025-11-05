package com.etsia.user.application.service;


import com.etsia.common.domain.model.UserDto;
import com.etsia.user.domain.repository.UserRepository;
import com.etsia.user.domain.service.UUserDomainService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserFindByIdService {

    //@Qualifier("uURepository")
    private final UserRepository userRepository;
    private final UUserDomainService UUserDomainService;


    public UserFindByIdService(UserRepository userRepository, UUserDomainService uUserDomainService) {
        this.userRepository = userRepository;
        UUserDomainService = uUserDomainService;
    }
    //private final UserDomainService userDomainService;

    public Optional<UserDto> exec(Integer id){
        if(!UUserDomainService.FindById(id).isPresent()){
            throw new IllegalArgumentException("User Not Found");
        }
        return userRepository.FindById(id);
    }
}
