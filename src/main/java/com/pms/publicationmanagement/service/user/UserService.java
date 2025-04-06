package com.pms.publicationmanagement.service.user;

import com.pms.publicationmanagement.dto.UpdatePasswordDto;
import com.pms.publicationmanagement.dto.UserDto;
import com.pms.publicationmanagement.dto.session.RegisterRequestDto;
import com.pms.publicationmanagement.dto.user.UpdateNameDto;
import com.pms.publicationmanagement.mapper.UserDtoMapper;
import com.pms.publicationmanagement.model.user.User;
import com.pms.publicationmanagement.model.user.UserRole;
import com.pms.publicationmanagement.repository.UserRepository;
import com.pms.publicationmanagement.service.encryption.EncryptionService;
import com.pms.publicationmanagement.service.resource.ResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    @Value("${resource-config.base-url}")
    private String imageBaseUrl;

    private final UserRepository userRepository;
    private final EncryptionService encryptionService;

    private final ResourceService resourceService;

    public User registerNewUser(RegisterRequestDto registerRequestDto) {
        User userToBeRegistered = new User();
        userToBeRegistered.setFirstName(registerRequestDto.firstName);
        userToBeRegistered.setMiddleName(registerRequestDto.middleName);
        userToBeRegistered.setLastName(registerRequestDto.lastName);
        userToBeRegistered.setEmail(registerRequestDto.email);
        userToBeRegistered.setPassword(encryptionService.encryptPassword(registerRequestDto.password));
        userToBeRegistered.setUserRole(UserRole.ROLE_INSTITUTION_ADMINISTRATOR);
        return userRepository.save(userToBeRegistered);
    }


    public void addUser(String firstName, String middleName, String lastName, String email,
                        String password, UserRole userRole) {
        var user = new User();
        user.setFirstName(firstName);
        user.setMiddleName(middleName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(password);
        user.setUserRole(userRole);

        userRepository.save(user);
    }
    public User searchUserByName(String firstName, String middleName, String lastName) {
        User user = userRepository.findByFirstNameAndMiddleNameAndLastName(firstName, middleName, lastName);

        if(user == null) {
            throw new RuntimeException("User with name not found");
        }

        return user;
    }

    public User findById(UUID id) {
        User user = userRepository.findById(id).orElse(null);

        if(user == null) {
            throw new RuntimeException("User with id not found");
        }

        return user;

    }

    public List<User> findAllUsers() { return userRepository.findAll(); }

    public void removeUser(UUID id) { userRepository.deleteById(id); }

    public UserDto updateUserNameById(UUID id, UpdateNameDto updateNameDto) {
        User user = findById(id);

        if (user == null) {
            throw new RuntimeException("User with id not found");
        }

        user.setFirstName(updateNameDto.getFirstName());
        user.setMiddleName(updateNameDto.getMiddleName());
        user.setLastName(updateNameDto.getLastName());
        userRepository.save(user);

        return UserDtoMapper.toUserDto(user);
    }

    public UserDto updatePasswordById(UUID id, UpdatePasswordDto updateNameDto) {
        User user = findById(id);

        if (user == null) {
            throw new RuntimeException("User with id not found");
        }

        if (!new BCryptPasswordEncoder().matches(updateNameDto.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("Old password does not match");
        }

        user.setPassword(encryptionService.encryptPassword(updateNameDto.getNewPassword()));

        userRepository.save(user);

        return UserDtoMapper.toUserDto(user);
    }

    public UserDto updateProfileImage(UUID id, MultipartFile file) {
        User user = findById(id);

        if (user == null) {
            throw new RuntimeException("User with id not found");
        }

        try {
            user.setImageUrl(String.format("%s/resources/profile/%s", imageBaseUrl,
                    resourceService.uploadProfileImage(id, file)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        userRepository.save(user);

        return UserDtoMapper.toUserDto(user);
    }
}