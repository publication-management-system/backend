package com.pms.publicationmanagement.service.institution;

import com.pms.publicationmanagement.dto.session.RegisterRequestDto;
import com.pms.publicationmanagement.model.user.Institution;
import com.pms.publicationmanagement.model.user.User;
import com.pms.publicationmanagement.repository.InstitutionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InstitutionService {
    private final InstitutionRepository institutionRepository;

    public Institution registerNewInstitution(RegisterRequestDto registerRequestDto,
                                                     User savedUser) {
        Institution institutionToBeRegistered = new Institution();
        institutionToBeRegistered.setName(registerRequestDto.institutionName);
        institutionToBeRegistered.setEmail(registerRequestDto.institutionEmailAddress);
        institutionToBeRegistered.setAddress(registerRequestDto.institutionAddress);
        institutionToBeRegistered.setPhoneNumber(registerRequestDto.institutionPhoneNumber);
        institutionToBeRegistered.setUsers(List.of(savedUser));

        return institutionRepository.save(institutionToBeRegistered);
    }

    public Institution addInstitution(String name, String address, String phoneNumber, String email) {
        Institution saved = new Institution();
        saved.setName(name);
        saved.setAddress(address);
        saved.setPhoneNumber(phoneNumber);
        saved.setEmail(email);
        institutionRepository.save(saved);
        return saved;
    }

    public Institution findByNameAndAddress(String name, String address) {
        Institution institution = institutionRepository.findByNameAndAddress(name, address);

        if (institution == null) {
            throw new RuntimeException("Institution with name and address not found");
        }

        return institution;
    }

    public Institution findByEmail(String email) {
        Institution institution = institutionRepository.findByEmail(email);

        if (institution == null) {
            throw new RuntimeException("Institution with email not found");
        }

        return institution;
    }

    public Institution findByPhoneNumber(String phoneNumber) {
        Institution institution = institutionRepository.findByPhoneNumber(phoneNumber);

        if (institution == null) {
            throw new RuntimeException("Institution with phone number not found");
        }

        return institution;
    }

    public List<Institution> findAll() {
        return institutionRepository.findAll();
    }

    public void deleteByNameAndAddress(String name, String address) {
        institutionRepository.deleteByNameAndAddress(name, address);
    }

}
