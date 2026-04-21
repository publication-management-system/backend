package com.pms.publicationmanagement.repository;

import com.pms.publicationmanagement.model.profiling.Citation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CitationRepository extends JpaRepository<Citation, UUID> {
    List<Citation> findByTitle(String title);
}
