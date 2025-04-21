package com.pms.publicationmanagement.repository;

import com.pms.publicationmanagement.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    User findByEmailAndPassword(String email, String password);

    User findByFirstNameAndMiddleNameAndLastName(String firstName, String middleName, String lastName);

    List<User> findAllByInstitutionId(UUID institutionId);

    User findByEmail(String email);
}
