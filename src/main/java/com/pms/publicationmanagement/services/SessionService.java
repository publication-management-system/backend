package com.pms.publicationmanagement.services;


import com.pms.publicationmanagement.model.Institution;
import com.pms.publicationmanagement.model.User;
import com.pms.publicationmanagement.model.UserType;
import com.pms.publicationmanagement.repository.SpringJpaInstitutionRepository;
import com.pms.publicationmanagement.repository.SpringJpaUserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class SessionService {
    private final SpringJpaUserRepository userRepository;

    private final SpringJpaInstitutionRepository institutionRepository;

    public SessionService(SpringJpaUserRepository userRepository, SpringJpaInstitutionRepository institutionRepository) {
        this.userRepository = userRepository;
        this.institutionRepository = institutionRepository;
    }

    @Transactional
    public User registerUser(String firstName, String middleName, String lastName, String email,
                             String password, UserType userType,
                             String institutionName, String address,
                             String phoneNumber, String institutionEmail) {

        User userToBeSaved = userRepository.save(new User(null, firstName, middleName, lastName, email, password, userType));
        Institution institutionToBeSaved = institutionRepository.save(new Institution(null, institutionName, address, phoneNumber, institutionEmail));

        userToBeSaved.setInstitution(institutionToBeSaved);
        return userToBeSaved;
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
