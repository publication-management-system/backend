package com.pms.publicationmanagement.repository;

import com.pms.publicationmanagement.model.user.Institution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface InstitutionRepository extends JpaRepository<Institution, UUID> {

    Institution findByNameAndAddress(String name, String address);

    Institution findByPhoneNumber(String phoneNumber);

    Institution findByEmail(String email);

    void deleteByNameAndAddress(String name, String address);
}
