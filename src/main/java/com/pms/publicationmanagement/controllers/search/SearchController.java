package com.pms.publicationmanagement.controllers.search;

import com.pms.publicationmanagement.dto.search.AuthorSearchResponse;
import com.pms.publicationmanagement.dto.search.DocumentsSearchResponse;
import com.pms.publicationmanagement.dto.shared.PagedResponse;
import com.pms.publicationmanagement.service.search.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search")
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/authors")
    public PagedResponse<AuthorSearchResponse> searchAuthors(@RequestParam String name,
                                                             @RequestParam(defaultValue = "1") int pageNumber,
                                                             @RequestParam(defaultValue = "10") int pageSize) {
        var result = searchService.searchAuthors(name, pageNumber - 1, pageSize);
        if (pageNumber <= result.getTotalPages() && pageNumber >= 1) {
            result.setPageNumber(pageNumber);
        }
        return result;
    }

    @GetMapping("/documents")
    public PagedResponse<DocumentsSearchResponse> searchDocuments(@RequestParam String name,
                                                                  @RequestParam(defaultValue = "1") int pageNumber,
                                                                  @RequestParam(defaultValue = "10") int pageSize) {
        var result = searchService.searchDocuments(name, pageNumber - 1, pageSize);
        if (pageNumber <= result.getTotalPages() && pageNumber >= 1) {
            result.setPageNumber(pageNumber);
        }
        return result;
    }
}
