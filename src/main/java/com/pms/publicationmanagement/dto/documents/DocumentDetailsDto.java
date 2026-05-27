package com.pms.publicationmanagement.dto.documents;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DocumentDetailsDto {
    private UUID id;
    private String title;
    private String publicationDate;
    private String issued;
    private String volume;
    private String issue;
    private String pages;
    private String publisher;
    private String description;
    private String link;
    private String googleScholarId;
    private String dblpId;
    private String wosId;
    private String internalRefId;
}
