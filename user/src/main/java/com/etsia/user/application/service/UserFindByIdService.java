package com.etsia.user.application.service;


import com.etsia.common.domain.model.UserDto;
import com.etsia.user.domain.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserFindByIdService {

    //@Qualifier("uURepository")
    private final UserRepository userRepository;

    public UserFindByIdService( UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    //private final UserDomainService userDomainService;

    public Optional<UserDto> exec(Integer id){
        return userRepository.FindById(id);
    }
}
