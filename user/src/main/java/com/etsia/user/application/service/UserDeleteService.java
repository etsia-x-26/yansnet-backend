package com.etsia.user.application.service;

import com.etsia.user.domain.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserDeleteService {

    //@Qualifier("uURepository")
    private final UserRepository userRepository;
    //private final UserDomainService userDomainService;

    public UserDeleteService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void execute(Integer id){
        try {
            userRepository.Delete(id);
        } catch (Exception e) {
            throw new IllegalArgumentException("User Not Found");
        }
    }
}
