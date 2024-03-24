package com.pms.publicationmanagement.repository;

import com.pms.publicationmanagement.model.Institution;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;


@Repository
public class InstitutionRepository {

    private final List<Institution> institutions;

    public InstitutionRepository() { institutions = new ArrayList<>(); }

    public void save(Institution institution) { institutions.add(institution); }

    public Institution findByNameAndAddress(String name, String address) {
        for(Institution institution : institutions) {
            if(institution.getName().equals(name) && institution.getAddress().equals(address)){
                return institution;
            }
        }
        return null;
    }

    public Institution findByPhoneNumber(String phoneNumber) {
        for(Institution institution : institutions) {
            if(institution.getPhoneNumber().equals(phoneNumber)){
                return institution;
            }
        }
        return null;
    }

    public Institution findByEmail(String email) {
        for(Institution institution : institutions) {
            if(institution.getEmail().equals(email)) {
                return institution;
            }
        }
        return null;
    }

    public void deleteByNameAndAddress(String name, String address) {
        for(Institution institution : institutions) {
            if(institution.getName().equals(name) && institution.getAddress().equals(address)){
                institutions.remove(institution);
                break;
            }
        }
    }

    public List<Institution> findAll() { return institutions; }
}
