package com.pms.publicationmanagement.service.profiling;

import com.pms.publicationmanagement.model.profiling.Author;
import com.pms.publicationmanagement.model.profiling.Document;
import com.pms.publicationmanagement.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;

    //bahaos cu id-urile (cateodata sunt la param, cateodata nu)
    public void addAuthor(UUID id, String name, String role, String institution, String institutionMail, List<Document> documents) {
        authorRepository.save(new Author(null, name, role, institution, institutionMail, documents));
    }

    public List<Author> getAuthorByName(String name) {
        return authorRepository.findByName(name);
    }

    public List<Author> getAuthorByRole(String role) {
        return authorRepository.findByRole(role);
    }

    public List<Author> getAuthorByInstitution(String institution) {
        return authorRepository.findByInstitution(institution);
    }

    public Author getAuthorByInstitutionMail(String institutionMail) {
        Author author = authorRepository.findByInstitutionMail(institutionMail);
        if(author == null) {
            throw new RuntimeException("author not found");
        }
        return author;
    }

    public void removeAuthor(UUID id) {
        authorRepository.deleteById(id);
    }

    public List<Author> getAll() {
        return authorRepository.findAll();
    }


}
