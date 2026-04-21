package com.pms.publicationmanagement.repository;

import com.pms.publicationmanagement.model.profiling.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AuthorRepository extends JpaRepository<Author, UUID> {
    List<Author> findByName(String name);

    List<Author> findByInstitutionRole(String role);

    List<Author> findByInstitution(String institution);

    Author findByInstitutionMail(String institutionMail);

    @Query("select a from Author a where a.googleScholarId = :providerId or a.dblpId = :providerId " +
            "or a.wosId = :providerId or a.internalRefId = :internalRefId")
    Optional<Author> findExisting(String internalRefId, String providerId);
}
