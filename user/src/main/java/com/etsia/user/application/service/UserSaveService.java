package com.etsia.user.application.service;


import com.etsia.common.domain.model.UserDto;
import com.etsia.user.domain.model.dto.request.user.CreateUserDto;
import com.etsia.user.domain.repository.UserRepository;
import com.etsia.user.domain.service.UserDomainService;
import com.etsia.user.infrastructure.exception.EmailAlreadyUsedException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import static com.etsia.user.infrastructure.config.MapperUser.mapToUserDto;

@Service
@AllArgsConstructor
public class UserSaveService {

    private final UserRepository userRepository;
    private final UserDomainService userDomainService;

    public UserDto execute(CreateUserDto user){
        if(!userDomainService.IsEmailUnique(user.getEmail())){
            throw new EmailAlreadyUsedException("Email already used");
        }

        // Correction ici : UserDto au lieu de User
        UserDto userCreated = userRepository.Save(user);
        return userCreated; // Pas besoin de mapper car c'est déjà un UserDto
    }
}