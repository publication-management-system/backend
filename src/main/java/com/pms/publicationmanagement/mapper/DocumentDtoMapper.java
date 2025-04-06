package com.pms.publicationmanagement.mapper;

import com.pms.publicationmanagement.dto.DocumentDto;
import com.pms.publicationmanagement.model.profiling.Document;

import java.util.ArrayList;
import java.util.List;

public class DocumentDtoMapper {

    public static DocumentDto toDocumentDto (Document document) {
        DocumentDto documentDto = new DocumentDto();
        documentDto.id = document.getId();
        documentDto.authors = document.getAuthors();
        documentDto.citedIn = document.getCitedIn();
        documentDto.description = document.getDescription();
        documentDto.title = document.getTitle();
        documentDto.issue = document.getIssue();
        documentDto.issued = document.getIssued();
        documentDto.pages = document.getPages();
        documentDto.publicationDate = document.getPublicationDate();
        documentDto.publisher = document.getPublisher();
        documentDto.volume = document.getVolume();
        return documentDto;
    }

    public static List<DocumentDto> toDocumentDtoList(List<Document> documentList) {
        List<DocumentDto> result = new ArrayList<>();
        for(Document d : documentList) {
            result.add(toDocumentDto(d));
        }
        return result;
    }
}
