package com.pms.publicationmanagement.mapper.search;

import com.pms.publicationmanagement.dto.search.DocumentsSearchResponse;
import com.pms.publicationmanagement.dto.shared.PagedResponse;
import com.pms.publicationmanagement.model.profiling.Document;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
public class DocumentSearchMapper {

    public static DocumentsSearchResponse toResponse(Document document) {
        if (document == null) {
            return null;
        }

        return new DocumentsSearchResponse(
                document.getId(),
                document.getTitle(),
                document.getPublisher(),
                document.getVolume()
        );
    }

    public static List<DocumentsSearchResponse> toResponseList(List<Document> documents) {
        if (documents == null || documents.isEmpty()) {
            return List.of();
        }

        return documents
                .stream()
                .map(DocumentSearchMapper::toResponse)
                .collect(Collectors.toList());
    }

    public static PagedResponse<DocumentsSearchResponse> toPagedResponse(Page<Document> page) {
        if (page == null || page.isEmpty() || page.getContent() == null) {
            return new PagedResponse<>(List.of(), 0L, 0L, 0);
        }

        List<DocumentsSearchResponse> list = page.getContent()
                .stream()
                .map(DocumentSearchMapper::toResponse)
                .toList();

        return new PagedResponse<>(
                list,
                page.getTotalElements(),
                (long) page.getTotalPages(),
                page.getNumber() + 1
        );
    }
}
