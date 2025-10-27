package com.etsia.user.application.service;

import com.etsia.user.domain.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserDeleteService {

    //@Qualifier("userRepositoryImpl")
    private final UserRepository userRepository;
    //private final UserDomainService userDomainService;

    public void execute(Integer id){
        try {
            userRepository.Delete(id);
        } catch (Exception e) {
            throw new IllegalArgumentException("User Not Found");
        }
    }
}
