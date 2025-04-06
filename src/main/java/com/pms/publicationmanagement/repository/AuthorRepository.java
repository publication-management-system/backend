package com.pms.publicationmanagement.repository;

import com.pms.publicationmanagement.model.profiling.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AuthorRepository extends JpaRepository<Author, UUID> {
    List<Author> findByName(String name);

    List<Author> findByRole(String role);

    List<Author> findByInstitution(String institution);

    Author findByInstitutionMail(String institutionMail);

}
