package com.etsia.user.application.service;


import com.etsia.common.domain.model.UserDto;
import com.etsia.user.domain.model.dto.request.user.CreateUserDto;
import com.etsia.user.domain.repository.UserRepository;
import com.etsia.user.domain.service.UUserDomainService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserSaveService {

    private final UserRepository userRepository;
    private final UUserDomainService UUserDomainService;

    public UserDto execute(CreateUserDto user){
        if(!UUserDomainService.IsEmailUnique(user.getEmail())){
            throw new IllegalArgumentException("Email already used");
        }

        // Correction ici : UserDto au lieu de User
        return userRepository.Save(user);
        //return userCreated; // Pas besoin de mapper car c'est déjà un UserDto
    }
}