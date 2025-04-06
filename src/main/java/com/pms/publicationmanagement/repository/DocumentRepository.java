package com.pms.publicationmanagement.repository;

import com.pms.publicationmanagement.model.profiling.Author;
import com.pms.publicationmanagement.model.profiling.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DocumentRepository extends JpaRepository<Document, UUID> {

    List<Document> findByTitle(String title);

    List<Document> findByPublicationDate(String publicationDate);

    //e mai ok sa sa fie param List<String> names?
    List<Document> findByAuthors(List<Author> authorList);

    List<Document> findByIssued(String issued);

    List<Document> findByPublisher(String publisher);

}
