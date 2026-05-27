package com.pms.publicationmanagement.controllers.publicendpoints;

import com.pms.publicationmanagement.dto.authors.AuthorDetailsDto;
import com.pms.publicationmanagement.dto.documents.DocumentDetailsDto;
import com.pms.publicationmanagement.dto.search.AuthorSearchResponse;
import com.pms.publicationmanagement.dto.shared.PagedResponse;
import com.pms.publicationmanagement.service.profiling.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/public/authors")
public class PublicAuthorController {

    private final AuthorService authorService;


    @GetMapping
    public AuthorDetailsDto getAuthorDetailsById(@RequestParam UUID authorId) {
        return authorService.findDetailsById(authorId);
    }

    @GetMapping("/minimal")
    public PagedResponse<AuthorSearchResponse> getAuthorsMinimalInfoPaged(@RequestParam Integer pageNumber,
                                                                          @RequestParam Integer pageSize) {
        return authorService.findAuthorsMinimalInfo(pageNumber - 1, pageSize);
    }


    @GetMapping("/documents")
    public PagedResponse<DocumentDetailsDto> getAuthorDetailsById(@RequestParam UUID authorId,
                                                                  @RequestParam(defaultValue = "1") int pageNumber,
                                                                  @RequestParam(defaultValue = "10") int pageSize) {
        return authorService.getDocumentsByAuthorPaged(authorId, pageNumber - 1, pageSize);
    }
}
