package com.pms.publicationmanagement.service.search;

import com.pms.publicationmanagement.dto.search.AuthorSearchResponse;
import com.pms.publicationmanagement.dto.search.DocumentsSearchResponse;
import com.pms.publicationmanagement.dto.shared.PagedResponse;
import com.pms.publicationmanagement.mapper.search.AuthorSearchMapper;
import com.pms.publicationmanagement.mapper.search.DocumentSearchMapper;
import com.pms.publicationmanagement.repository.AuthorRepository;
import com.pms.publicationmanagement.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchService {

    private final AuthorRepository authorRepository;
    private final DocumentRepository documentsRepository;

    public PagedResponse<AuthorSearchResponse> searchAuthors(String name, int pageNumber, int pageSize) {
        var foundAuthors = authorRepository.searchAuthorByName(name, PageRequest.of(pageNumber, pageSize));

        return AuthorSearchMapper.toPagedResponse(foundAuthors);
    }

    public PagedResponse<DocumentsSearchResponse> searchDocuments(String name, int pageNumber, int pageSize) {
        var foundDocuments = documentsRepository.searchDocumentsByName(name, PageRequest.of(pageNumber, pageSize));

        return DocumentSearchMapper.toPagedResponse(foundDocuments);
    }
}
