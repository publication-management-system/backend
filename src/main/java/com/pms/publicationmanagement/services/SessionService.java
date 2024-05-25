package com.pms.publicationmanagement.services;


import com.pms.publicationmanagement.model.Institution;
import com.pms.publicationmanagement.model.User;
import com.pms.publicationmanagement.model.UserType;
import com.pms.publicationmanagement.repository.SpringJpaInstitutionRepository;
import com.pms.publicationmanagement.repository.SpringJpaUserRepository;
import com.pms.publicationmanagement.services.encryption.EncryptionService;
import com.pms.publicationmanagement.services.tokens.TokenService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SessionService {
    private final SpringJpaUserRepository userRepository;

    private final SpringJpaInstitutionRepository institutionRepository;

    private final EncryptionService encryptionService;

    private final TokenService tokenService;

    @Transactional
    public User registerUser(String firstName, String middleName, String lastName, String email,
                             String password, UserType userType,
                             String institutionName, String address,
                             String phoneNumber, String institutionEmail) {

        User userToBeSaved = userRepository.save(
                new User(null, firstName, middleName, lastName, email, encryptionService.encryptPassword(password), userType)
        );
        Institution institutionToBeSaved = institutionRepository.save(new Institution(null, institutionName, address, phoneNumber, institutionEmail));

        userToBeSaved.setInstitution(institutionToBeSaved);
        return userToBeSaved;
    }

    public String loginUser(String email, String password) {
        User user = userRepository.findByEmail(email);

        if(user == null) {
            throw new RuntimeException("User not found");
        }

        if (!encryptionService.isSamePassword(password, user.getPassword())) {
            throw new RuntimeException("Incorrect password");
        }

        return tokenService.generateToken(user);
    }

}
