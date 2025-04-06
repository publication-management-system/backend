package com.pms.publicationmanagement.service.profiling;


import com.pms.publicationmanagement.dto.session.RegisterRequestDto;
import com.pms.publicationmanagement.dto.session.RegisterResponseDto;
import com.pms.publicationmanagement.model.user.Institution;
import com.pms.publicationmanagement.model.user.User;
import com.pms.publicationmanagement.repository.InstitutionRepository;
import com.pms.publicationmanagement.repository.UserRepository;
import com.pms.publicationmanagement.service.encryption.EncryptionService;
import com.pms.publicationmanagement.service.institution.InstitutionService;
import com.pms.publicationmanagement.service.tokens.TokenService;
import com.pms.publicationmanagement.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SessionService {
    private final UserRepository userRepository;

    private final InstitutionRepository institutionRepository;

    private final EncryptionService encryptionService;

    private final TokenService tokenService;
    private final UserService userService;
    private final InstitutionService institutionService;

    @Transactional
    public RegisterResponseDto registerUser(RegisterRequestDto registerRequestDto) {
        User savedUser = userService.registerNewUser(registerRequestDto);
        Institution savedInstitution = institutionService.registerNewInstitution(registerRequestDto, savedUser);
        savedUser.setInstitution(savedInstitution);

        return new RegisterResponseDto(savedUser.getId(), savedInstitution.getId());
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
