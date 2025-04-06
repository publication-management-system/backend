package com.pms.publicationmanagement.service.profiling;

import com.pms.publicationmanagement.model.profiling.Author;
import com.pms.publicationmanagement.model.profiling.Citation;
import com.pms.publicationmanagement.model.profiling.Document;
import com.pms.publicationmanagement.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;
    public void addDocument(UUID id, String title, String publicationDate, List<Author> authors,
                            String issued, String volume, String issue, String pages, String publisher,
                            String description, List<Citation> citedIn, String link) {

        Document saved = new Document(id, title, publicationDate, authors,
                issued, volume, issue, pages, publisher,
                description, link, citedIn);

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

    public void removeAuthor(UUID id) {
        documentRepository.deleteById(id);
    }

    public List<Document> getAll() {
        return documentRepository.findAll();
    }
}
