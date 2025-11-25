package com.etsia.user.infrastructure.repository;


import com.etsia.common.domain.model.UserDto;
import com.etsia.common.domain.model.sub.Email;
import com.etsia.common.domain.model.sub.PhoneNumber;
import com.etsia.common.infrastructure.entities.Batch;
import com.etsia.common.infrastructure.entities.Department;
import com.etsia.common.infrastructure.entities.User;
import com.etsia.common.infrastructure.entities.UserCategory;
import com.etsia.user.domain.model.dto.request.user.CreateUserDto;
import com.etsia.user.domain.model.dto.request.user.UserUpdateDto;
import com.etsia.user.domain.repository.UserRepository;
import jdk.jfr.Category;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.etsia.user.infrastructure.config.MapperUser.mapToUserDto;

@Repository(value = "uURepository")
//@Repository
public class UserRepositoryImpl implements UserRepository {


    private final JpaUUserCategorieRepository jpaUUserCategorieRepository;
    private final JpaUUserRepository jpaUUserRepository;
    private final JpaBBatchRepository jpaBBatchRepository;
    private final JpaDDepartementRepository jpaDDepartmentRepository;

    public UserRepositoryImpl(
            @Qualifier("UCJRepository") JpaUUserCategorieRepository jpaUUserCategorieRepository,
            @Qualifier("uJRepository") JpaUUserRepository jpaUUserRepository,
            @Qualifier("UBRepository") JpaBBatchRepository jpaBBatchRepository,
            @Qualifier("UDJRepository") JpaDDepartementRepository jpaDDepartmentRepository) {
        this.jpaUUserCategorieRepository = jpaUUserCategorieRepository;
        this.jpaUUserRepository = jpaUUserRepository;
        this.jpaBBatchRepository = jpaBBatchRepository;
        this.jpaDDepartmentRepository = jpaDDepartmentRepository;
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
        System.out.println(user);
        return Optional.ofNullable(mapToUserDto(user));
    }

    @Override
    public UserDto Save(CreateUserDto createUserDto) {

        // Convertir les value objects
        Email email = new Email(createUserDto.getEmail());
        PhoneNumber phone = new PhoneNumber(createUserDto.getPhoneNumber());

        // Vérifier que les relations existent
        UserCategory category = jpaUUserCategorieRepository.findById(createUserDto.getCategory_id())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Department department = jpaDDepartmentRepository.findById(createUserDto.getDepartment_id())
                .orElseThrow(() -> new RuntimeException("Department not found"));

        Batch batch = jpaBBatchRepository.findById(createUserDto.getBatch_id())
                .orElseThrow(() -> new RuntimeException("Batch not found"));

        // Créer une nouvelle instance de User
        User userEntity = new User();

        // Si tu utilises une génération automatique @GeneratedValue, NE PAS définir l'id
        userEntity.setId(createUserDto.getId());

        userEntity.setEmail(email);
        userEntity.setPassword(createUserDto.getPassword());

        userEntity.setCategory(category);
        userEntity.setDepartment(department);
        userEntity.setBatch(batch);

        // Champs simples
        userEntity.setName(createUserDto.getName());
        userEntity.setUsername(createUserDto.getUsername());
        userEntity.setDescription(createUserDto.getDescription());
        userEntity.setUrlprofile(createUserDto.getUrlprofile());
        userEntity.setPhoneNumber(phone);

        // Valeurs par défaut
        userEntity.setIsActive(true);
        userEntity.setIsBlocked(false);
        userEntity.setTotalFollowers(0);
        userEntity.setTotalFollowing(0);
        userEntity.setTotalPosts(0);

        // Sauvegarde
        User savedUser = jpaUUserRepository.save(userEntity);

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
    public UserDto update(UserUpdateDto userDto) {

        User user = jpaUUserRepository.findById(userDto.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // EMAIL
        if (userDto.getEmail() != null) {
            user.setEmail(new Email(userDto.getEmail()));
        }

        // PASSWORD
        if (userDto.getPassword() != null) {
            user.setPassword(userDto.getPassword());
        }

        // ACTIVE
        if (userDto.getIsActive() != null) {
            user.setIsActive(userDto.getIsActive());
        }

        // BLOCKED
        if (userDto.getIsBlocked() != null) {
            user.setIsBlocked(userDto.getIsBlocked());
        }

        // CATEGORY (ID → entity)
        if (userDto.getCategory_id() != null) {
            UserCategory category = jpaUUserCategorieRepository.findById(userDto.getCategory_id())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            user.setCategory(category);
        }

        // DEPARTMENT (ID → entity)
        if (userDto.getDepartment_id() != null) {
            Department dep = jpaDDepartmentRepository.findById(userDto.getDepartment_id())
                    .orElseThrow(() -> new RuntimeException("Department not found"));
            user.setDepartment(dep);
        }

        // BATCH (ID → entity)
        if (userDto.getBatch_id() != null) {
            Batch batch = jpaBBatchRepository.findById(userDto.getBatch_id())
                    .orElseThrow(() -> new RuntimeException("Batch not found"));
            user.setBatch(batch);
        }

        // PHONE NUMBER
        if (userDto.getPhoneNumber() != null) {
            user.setPhoneNumber(new PhoneNumber(userDto.getPhoneNumber()));
        }

        // NAME
        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }

        // USERNAME
        if (userDto.getUsername() != null) {
            user.setUsername(userDto.getUsername());
        }

        // DESCRIPTION
        if (userDto.getDescription() != null) {
            user.setDescription(userDto.getDescription());
        }

        // URL PROFILE
        if (userDto.getUrlprofile() != null) {
            user.setUrlprofile(userDto.getUrlprofile());
        }

        // FOLLOWERS
        if (userDto.getTotalFollowers() != null) {   // ⚠️ DTO must use Integer
            user.setTotalFollowers(userDto.getTotalFollowers());
        }

        // FOLLOWING
        if (userDto.getTotalFollowing() != null) {   // ⚠️ DTO must use Integer
            user.setTotalFollowing(userDto.getTotalFollowing());
        }

        // POSTS
        if (userDto.getTotalPosts() != null) {       // ⚠️ DTO must use Integer
            user.setTotalPosts(userDto.getTotalPosts());
        }

        User userUpdated = jpaUUserRepository.save(user);
        return mapToUserDto(userUpdated);
    }


    @Override
    public void Delete(Integer id) {
        jpaUUserRepository.deleteById(id);
    }


}