package com.pms.publicationmanagement.services;

import com.pms.publicationmanagement.model.Institution;
import com.pms.publicationmanagement.repository.SpringJpaInstitutionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InstitutionService {
    private final SpringJpaInstitutionRepository institutionRepository;
    public Institution addInstitution(String name, String address, String phoneNumber, String email) {
        Institution saved = new Institution(null, name, address, phoneNumber, email);
        institutionRepository.save(saved);
        return saved;
    }

    public Institution findByNameAndAddress(String name, String address) {
        Institution institution = institutionRepository.findByNameAndAddress(name, address);

        if(institution == null ){
            throw new RuntimeException("Institution with name and address not found");
        }

        return institution;
    }

    public Institution findByEmail(String email) {
        Institution institution = institutionRepository.findByEmail(email);

        if(institution == null ) {
            throw new RuntimeException("Institution with email not found");
        }

        return institution;
    }

    public Institution findByPhoneNumber(String phoneNumber) {
        Institution institution = institutionRepository.findByPhoneNumber(phoneNumber);

        if(institution == null) {
            throw new RuntimeException("Institution with phone number not found");
        }

        return institution;
    }

    public List<Institution> findAll() { return institutionRepository.findAll(); }

    public void deleteByNameAndAddress(String name, String address) { institutionRepository.deleteByNameAndAddress(name, address); }

}
