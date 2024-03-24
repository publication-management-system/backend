package com.pms.publicationmanagement.services;


import com.pms.publicationmanagement.model.Institution;
import com.pms.publicationmanagement.model.User;
import com.pms.publicationmanagement.model.UserType;
import com.pms.publicationmanagement.repository.SpringJpaInstitutionRepository;
import com.pms.publicationmanagement.repository.SpringJpaUserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SessionService {
    private final SpringJpaUserRepository userRepository;

    private final SpringJpaInstitutionRepository institutionRepository;

    public SessionService(SpringJpaUserRepository userRepository, SpringJpaInstitutionRepository institutionRepository) {
        this.userRepository = userRepository;
        this.institutionRepository = institutionRepository;
    }

    public User registerUser(String firstName, String middleName, String lastName, String email,
                             String password, Integer id, UserType userType,
                             String institutionName, String address,
                             String phoneNumber, String institutionEmail) {

        User userToBeSaved = new User(firstName, middleName, lastName, email, password, id, userType);
        Institution institutionToBeSaved = new Institution(null, institutionName, address, phoneNumber, institutionEmail);
        institutionToBeSaved.getUsers().add(userToBeSaved);
        userToBeSaved.setInstitution(institutionToBeSaved);

        institutionRepository.save(institutionToBeSaved);
        return userRepository.save(userToBeSaved);
    }

    public User loginUser(String email, String password) {
        User user = userRepository.findByEmailAndPassword(email, password);

        if(user == null) {
            throw new RuntimeException("User not found");
        }

        System.out.println(String.format("%s has been logged in\n", user.getName()));

        return user;

    }

}
