package com.etsia.user.application.service;


import com.etsia.common.domain.model.UserDto;
import com.etsia.user.domain.model.dto.request.user.UserUpdateDto;
import com.etsia.user.domain.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserupdateService {

    private final UserRepository userRepository;
    //private final UserDomainService userDomainService;

    public UserDto exec(UserUpdateDto user){
        return userRepository.update(user);
    }
}
