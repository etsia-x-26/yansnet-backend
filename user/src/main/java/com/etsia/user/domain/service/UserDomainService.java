package com.etsia.user.domain.service;

import com.etsia.common.domain.model.UserDto;
import com.etsia.common.domain.model.sub.Email;
import com.etsia.user.domain.model.dto.request.user.CreateUserDto;
import com.etsia.user.domain.model.dto.request.user.UserUpdateDto;
import com.etsia.user.domain.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserDomainService {

    private final UserRepository userRepository;

    public Boolean IsEmailUnique(String email){
        return !userRepository.existsByEmail(email);
    }

    public Optional<UserDto> FindById(Integer id) {
        return userRepository.FindById(id);
    }

    public Optional<UserDto> FindByEmail(String email) {
        return userRepository.FindByEmail(email);
    }

    public UserDto Save(CreateUserDto user) {
        return userRepository.Save(user);
    }

    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public Optional<UserDto> FindByEmailAndPassword(String email, String password) {
        return userRepository.FindByEmailAndPassword(email, password);
    }

    public UserDto update(UserUpdateDto user) {
        return userRepository.update(user);
    }

    public void Delete(Integer id) {
        userRepository.Delete(id);
    }
}
