package com.pms.publicationmanagement.mapper.profiling;

import com.pms.publicationmanagement.dto.documents.DocumentDetailsDto;
import com.pms.publicationmanagement.dto.shared.PagedResponse;
import com.pms.publicationmanagement.model.profiling.Document;
import org.springframework.data.domain.Page;

import java.util.List;

public class DocumentDetailsMapper {
    public static DocumentDetailsDto toDocumentDetails(Document document) {
        if (document == null) {
            return null;
        }

        return new DocumentDetailsDto(
                document.getId(),
                document.getTitle(),
                document.getPublicationDate(),
                document.getIssued(),
                document.getVolume(),
                document.getIssue(),
                document.getPages(),
                document.getPublisher(),
                document.getDescription(),
                document.getLink(),
                document.getGoogleScholarId(),
                document.getDblpId(),
                document.getWosId(),
                document.getInternalRefId()
        );
    }

    public static PagedResponse<DocumentDetailsDto> toPagedDetails(Page<Document> page) {
        if (page == null || page.isEmpty() || page.getContent() == null) {
            return new PagedResponse<>(List.of(), 0L, 0L, 0);
        }

        List<DocumentDetailsDto> list = page.getContent()
                .stream()
                .map(DocumentDetailsMapper::toDocumentDetails)
                .toList();

        return new PagedResponse<>(
                list,
                page.getTotalElements(),
                (long) page.getTotalPages(),
                page.getNumber() + 1
        );
    }
}
