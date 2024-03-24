package com.pms.publicationmanagement.repository;

import com.pms.publicationmanagement.model.Institution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringJpaInstitutionRepository extends JpaRepository<Institution, Integer> {

    Institution findByNameAndAddress(String name, String address);

    Institution findByPhoneNumber(String phoneNumber);

    Institution findByEmail(String email);

    void deleteByNameAndAddress(String name, String address);
}
