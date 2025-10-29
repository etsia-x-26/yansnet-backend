package com.etsia.user.application.service;


import com.etsia.common.domain.model.UserDto;
import com.etsia.user.domain.model.dto.request.user.UserUpdateDto;
import com.etsia.user.domain.repository.UserRepository;
import com.etsia.user.domain.service.UUserDomainService;
import org.springframework.stereotype.Service;

@Service
public class UserupdateService {

    //@Qualifier("uURepository")
    private final UserRepository userRepository;
    private final UUserDomainService UUserDomainService;

    public UserupdateService(UserRepository userRepository, UUserDomainService uUserDomainService) {
        this.userRepository = userRepository;
        UUserDomainService = uUserDomainService;
    }
    //private final UserDomainService userDomainService;

    public UserDto exec(UserUpdateDto user){
        if(!UUserDomainService.FindById(user.getId()).isPresent()){
            throw new IllegalArgumentException("User not found");
        }

        return userRepository.update(user);
    }
}
