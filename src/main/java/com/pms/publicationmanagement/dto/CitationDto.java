package com.pms.publicationmanagement.dto;

import com.pms.publicationmanagement.model.Document;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

public class CitationDto {

    public Integer id;

    public String title;

    public String link;

    @ManyToOne
    @JoinColumn(name="document_id")
    public Document document;

    public CitationDto(Integer id, String title, String link, Document document) {
        this.id = id;
        this.title = title;
        this.link = link;
        this.document = document;
    }

    public CitationDto() {
    }
}
