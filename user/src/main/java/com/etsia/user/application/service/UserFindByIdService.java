package com.etsia.user.application.service;


import com.etsia.common.domain.model.UserDto;
import com.etsia.user.domain.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserFindByIdService {

    private final UserRepository userRepository;
    //private final UserDomainService userDomainService;

    public Optional<UserDto> exec(Integer id){
        return userRepository.FindById(id);
    }
}
