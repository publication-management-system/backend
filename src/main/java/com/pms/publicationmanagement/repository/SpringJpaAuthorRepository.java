package com.pms.publicationmanagement.repository;

import com.pms.publicationmanagement.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpringJpaAuthorRepository extends JpaRepository<Author, Integer> {
    List<Author> findByName(String name);

    List<Author> findByRole(String role);

    List<Author> findByInstitution(String institution);

    Author findByInstitutionMail(String institutionMail);

}
