package com.pms.publicationmanagement.repository;

import com.pms.publicationmanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SpringJpaUserRepository extends JpaRepository<User, Integer> {
    User findByEmailAndPassword(String email, String password);

    User findByFirstNameAndMiddleNameAndLastName(String firstName, String middleName, String lastName);

    User findByEmail(String email);
}
