package com.pms.publicationmanagement.service.profiling;

import com.pms.publicationmanagement.dto.authors.AuthorDetailsDto;
import com.pms.publicationmanagement.dto.documents.DocumentDetailsDto;
import com.pms.publicationmanagement.dto.search.AuthorSearchResponse;
import com.pms.publicationmanagement.dto.shared.PagedResponse;
import com.pms.publicationmanagement.mapper.profiling.AuthorDetailsMapper;
import com.pms.publicationmanagement.mapper.profiling.DocumentDetailsMapper;
import com.pms.publicationmanagement.mapper.search.AuthorSearchMapper;
import com.pms.publicationmanagement.model.profiling.Author;
import com.pms.publicationmanagement.model.profiling.Document;
import com.pms.publicationmanagement.repository.AuthorRepository;
import com.pms.publicationmanagement.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final DocumentRepository documentRepository;

    //bahaos cu id-urile (cateodata sunt la param, cateodata nu)
    public void addAuthor(UUID id, String name, String role, String institution, String institutionMail, List<Document> documents) {
//        authorRepository.save(new Author(UUID.randomUUID(), name, role, institution, institutionMail, documents));
    }

    public List<Author> getAuthorByName(String name) {
        return authorRepository.findByName(name);
    }

    public List<Author> getAuthorByRole(String role) {
        return authorRepository.findByInstitutionRole(role);
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

    public AuthorDetailsDto findDetailsById(UUID authorId) {
        var author = authorRepository.findById(authorId);

        if (author.isPresent()) {
            return AuthorDetailsMapper.toAuthorDetails(author.get());
        }

        throw new RuntimeException("Author not found");
    }

    public PagedResponse<DocumentDetailsDto> getDocumentsByAuthorPaged(UUID authorId, int pageNumber, int pageSize) {
        var results = documentRepository.findDistinctByAuthors_Id(authorId, PageRequest.of(pageNumber, pageSize));

        return DocumentDetailsMapper.toPagedDetails(results);
    }

    public PagedResponse<AuthorSearchResponse> findAuthorsMinimalInfo(Integer pageNumber, Integer pageSize) {
        var foundAuthors = authorRepository.findAll(PageRequest.of(pageNumber, pageSize));

        return AuthorSearchMapper.toPagedResponse(foundAuthors);
    }
}
