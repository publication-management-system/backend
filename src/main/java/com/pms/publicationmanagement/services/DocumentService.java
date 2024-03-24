package com.pms.publicationmanagement.services;

import com.pms.publicationmanagement.model.Author;
import com.pms.publicationmanagement.model.Citation;
import com.pms.publicationmanagement.model.Document;
import com.pms.publicationmanagement.repository.SpringJpaDocumentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentService {

    private final SpringJpaDocumentRepository documentRepository;

    public DocumentService(SpringJpaDocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    public void addDocument(Integer id, String title, String publicationDate, List<Author> authors,
                       String issued, String volume, String issue, String pages, String publisher,
                       String description, List<Citation> citedIn) {

        Document saved = new Document(id, title, publicationDate, authors,
                issued, volume, issue, pages, publisher,
                description,  citedIn);

        documentRepository.save(saved);
    }

    public List<Document> getByTitle(String title) {
        return documentRepository.findByTitle(title);
    }

    public List<Document> getByPublicationDate(String publicationDate) {
        return documentRepository.findByPublicationDate(publicationDate);
    }

    public List<Document> getByAuthors(List<Author> authors) {
        return documentRepository.findByAuthors(authors);
    }

    public List<Document> getByIssued(String issued) {
        return documentRepository.findByIssued(issued);
    }

    public List<Document> getByPublisher(String publisher) {
        return documentRepository.findByPublisher(publisher);
    }

    public void removeAuthor(Integer id) {
        documentRepository.deleteById(id);
    }

    public List<Document> getAll() {
        return documentRepository.findAll();
    }
}
