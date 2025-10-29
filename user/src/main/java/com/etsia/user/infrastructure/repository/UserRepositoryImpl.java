package com.etsia.user.infrastructure.repository;


import com.etsia.common.domain.model.UserDto;
import com.etsia.common.domain.model.sub.Email;
import com.etsia.common.infrastructure.entities.User;
import com.etsia.user.domain.model.dto.request.user.CreateUserDto;
import com.etsia.user.domain.model.dto.request.user.UserUpdateDto;
import com.etsia.user.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.etsia.user.infrastructure.config.MapperUser.mapToUserDto;

@Repository(value = "uURepository")
//@Repository
public class UserRepositoryImpl implements UserRepository {


    private final JpaUUserRepository jpaUUserRepository;

    public UserRepositoryImpl(
            @Qualifier("uJRepository")
            JpaUUserRepository jpaUUserRepository) {
        this.jpaUUserRepository = jpaUUserRepository;
    }

    @Override
    public Optional<UserDto> FindById(Integer id) {
        User user = jpaUUserRepository.findById(id).orElse(null);
        return Optional.ofNullable(mapToUserDto(user));
    }

    @Override
    public Optional<UserDto> FindByEmail(String email) {
        Email email_ = new Email(email);
        User user = jpaUUserRepository.findByEmail(email_);
        return Optional.ofNullable(mapToUserDto(user));
    }

    @Override
    public UserDto Save(CreateUserDto createUserDto) {
        Email email = new Email(createUserDto.getEmail());
        // Créer une nouvelle instance de User
        User userEntity = new User();
        // Mapper les champs du DTO vers l'entité
        userEntity.setId(createUserDto.getId());
        userEntity.setEmail(email);
        userEntity.setPassword(createUserDto.getPassword());
        //userEntity.setId(createUserDto.getId());
        // Définir les valeurs par défaut si nécessaire
        userEntity.setIsActive(true);
        userEntity.setIsBlocked(false);
        userEntity.setTotalFollowers(0);
        userEntity.setTotalFollowing(0);
        userEntity.setTotalPosts(0);
        
        // Sauvegarder l'entité
        User savedUser = jpaUUserRepository.save(userEntity);
        
        // Convertir en DTO et retourner
        return mapToUserDto(savedUser);
    }

    @Override
    public Boolean existsByEmail(String email) {
        Email email_ = new Email(email);
        return jpaUUserRepository.existsByEmail(email_);
    }

    @Override
    public Optional<UserDto> FindByEmailAndPassword(String email, String password) {
        Email email_ = new Email(email);
        User user_profile = jpaUUserRepository.findByEmail(email_);
        if (user_profile != null && user_profile.getPassword().equals(password)) { throw new RuntimeException("User not found");}
        
        return Optional.ofNullable(mapToUserDto(user_profile));
    }

    @Override
    public UserDto update(UserUpdateDto user) {
        Email email_ = new Email(user.getEmail());
        User user_update = new User();
        user_update.setId(user.getId());
        user_update.setEmail(email_);
        user_update.setPassword(user.getPassword());
        User user_update_ = jpaUUserRepository.save(user_update);
        return mapToUserDto(user_update_);
    }

    @Override
    public void Delete(Integer id) {
        jpaUUserRepository.deleteById(id);
    }


}