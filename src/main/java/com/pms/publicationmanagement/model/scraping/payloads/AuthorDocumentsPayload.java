package com.pms.publicationmanagement.model.scraping.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorDocumentsPayload {

    private String title;
    private String publicationDate;
    private List<String> coAuthorsNames;
    private String issued;
    private String volume;
    private String issue;
    private String pages;
    private String publisher;
    private String description;
    private String link;

}