package com.pms.publicationmanagement.repository;

import com.pms.publicationmanagement.model.profiling.Citation;
import com.pms.publicationmanagement.repository.projections.CitationsByYearProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CitationRepository extends JpaRepository<Citation, UUID> {
    List<Citation> findByTitle(String title);

    boolean existsByDocumentIdAndLink(UUID documentId, String citationsLink);

    @Query(value = """
        SELECT COUNT(DISTINCT c.id)
        FROM document d
        JOIN document_authors da 
            ON da.document_id = d.id
        JOIN citation c 
            ON c.document_id = d.id
        WHERE da.author_id = :authorId
    """, nativeQuery = true)
    Long findTotalCitationCountByAuthorId(UUID authorId);

    @Query(value = """
        SELECT
            SUBSTRING(d.publication_date, 1, 4) AS year,
            COUNT(c.id) AS citationsCount
        FROM document d
        JOIN document_authors da ON da.document_id = d.id
        JOIN citation c ON c.document_id = d.id
        WHERE da.author_id = :authorId
          AND d.publication_date IS NOT NULL
        GROUP BY SUBSTRING(d.publication_date, 1, 4)
        ORDER BY year
    """, nativeQuery = true)
    List<CitationsByYearProjection> getCitationsByYearForAuthor(UUID authorId);

    List<Citation> findAllByDocumentId(UUID documentId);
}
