package com.pms.publicationmanagement.services;

import com.pms.publicationmanagement.model.User;
import com.pms.publicationmanagement.model.UserType;
import com.pms.publicationmanagement.repository.SpringJpaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final SpringJpaUserRepository userRepository;
    public void addUser(String firstName, String middleName, String lastName, String email,
                        String password, Integer id, UserType userType) {
        userRepository.save(new User(id, firstName, middleName, lastName, email,
                password, userType));
    }
    public User searchUserByName(String firstName, String middleName, String lastName) {
        User user = userRepository.findByFirstNameAndMiddleNameAndLastName(firstName, middleName, lastName);

        if(user == null) {
            throw new RuntimeException("User with name not found");
        }

        return user;
    }

    public User findById(Integer id) {
        User user = userRepository.findById(id).orElse(null);

        if(user == null) {
            throw new RuntimeException("User with id not found");
        }

        return user;

    }

    public List<User> findAllUsers() { return userRepository.findAll(); }

    public void removeUser(Integer id) { userRepository.deleteById(id); }


}