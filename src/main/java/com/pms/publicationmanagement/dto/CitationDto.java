package com.pms.publicationmanagement.dto;

import com.pms.publicationmanagement.model.profiling.Document;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.util.UUID;

public class CitationDto {

    public UUID id;

    public String title;

    public String link;

    @ManyToOne
    @JoinColumn(name="document_id")
    public Document document;

    public CitationDto(UUID id, String title, String link, Document document) {
        this.id = id;
        this.title = title;
        this.link = link;
        this.document = document;
    }

    public CitationDto() {
    }
}
