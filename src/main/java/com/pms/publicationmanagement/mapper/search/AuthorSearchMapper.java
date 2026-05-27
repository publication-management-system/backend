package com.pms.publicationmanagement.mapper.search;

import com.pms.publicationmanagement.dto.search.AuthorSearchResponse;
import com.pms.publicationmanagement.dto.shared.PagedResponse;
import com.pms.publicationmanagement.model.profiling.Author;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
public class AuthorSearchMapper {

    public static AuthorSearchResponse toResponse(Author author) {
        if (author == null) {
            return null;
        }

        return new AuthorSearchResponse(author.getId(), author.getName(), author.getInstitution(), author.getImageUrl());
    }

    public static List<AuthorSearchResponse> toResponseList(List<Author> authors) {
        if (authors == null || authors.isEmpty()) {
            return List.of();
        }

        return authors
                .stream()
                .map(AuthorSearchMapper::toResponse)
                .collect(Collectors.toList());
    }

    public static PagedResponse<AuthorSearchResponse> toPagedResponse(Page<Author> page) {
        if (page == null || page.isEmpty() || page.getContent() == null) {

            return new PagedResponse<>(List.of(), 0L, 0L, 0);
        }

        List<AuthorSearchResponse> list = page.getContent()
                .stream()
                .map(AuthorSearchMapper::toResponse)
                .toList();

        return new PagedResponse<>(list, page.getTotalElements(), (long) page.getTotalPages(), page.getNumber() + 1);
    }
}
