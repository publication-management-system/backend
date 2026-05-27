package com.pms.publicationmanagement.repository;

import com.pms.publicationmanagement.model.profiling.Author;
import com.pms.publicationmanagement.model.profiling.Document;
import com.pms.publicationmanagement.repository.projections.DocumentsByYearProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DocumentRepository extends JpaRepository<Document, UUID> {

    List<Document> findByTitle(String title);

    List<Document> findByPublicationDate(String publicationDate);

    //e mai ok sa sa fie param List<String> names?
    List<Document> findByAuthors(List<Author> authorList);

    List<Document> findByIssued(String issued);

    List<Document> findByPublisher(String publisher);

    @Query("select d from Document d where d.googleScholarId = :providerId or d.dblpId = :providerId " +
            "or d.wosId = :providerId or d.internalRefId = :internalRefId")
    Optional<Author> findExisting(String internalRefId, String providerId);

    @Query(value = "SELECT * FROM document WHERE MATCH(title) AGAINST (CONCAT(:title, '*') IN BOOLEAN MODE) OR SOUNDEX(title) = SOUNDEX(:title)", nativeQuery = true)
    Page<Document> searchDocumentsByName(@Param("title") String title, Pageable pageRequest);

    Page<Document> findDistinctByAuthors_Id(UUID authorId, Pageable pageable);

    Optional<Document> findByGoogleScholarId(String providerId);

    Optional<Document> findByDblpId(String providerId);

    Optional<Document> findByWosId(String providerId);

    @Query(value = """
        SELECT
            SUBSTRING(d.publication_date, 1, 4) AS year,
            COUNT(DISTINCT d.id) AS documentCount
        FROM document d
        JOIN document_authors da
            ON da.document_id = d.id
        WHERE da.author_id = :authorId
          AND d.publication_date IS NOT NULL
        GROUP BY SUBSTRING(d.publication_date, 1, 4)
        ORDER BY year
    """, nativeQuery = true)
    List<DocumentsByYearProjection> findDocStatsByYearAndAuthorId(UUID authorId);
}
