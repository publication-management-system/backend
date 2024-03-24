package com.pms.publicationmanagement.dto;

import com.pms.publicationmanagement.model.Author;
import com.pms.publicationmanagement.model.Citation;
import jakarta.persistence.ManyToMany;

import java.util.List;

public class DocumentDto {

    public Integer id;
    public String title;
    public String publicationDate;
    public List<Author> authors;
    public String issued;
    public String volume;
    public String issue;
    public String pages;
    public String publisher;
    public String description;
    public List<Citation> citedIn;

    public DocumentDto() {
    }

    public DocumentDto(Integer id, String title, String publicationDate, List<Author> authors, String issued,
                       String volume, String issue, String pages, String publisher, String description,
                       List<Citation> citedIn) {
        this.id = id;
        this.title = title;
        this.publicationDate = publicationDate;
        this.authors = authors;
        this.issued = issued;
        this.volume = volume;
        this.issue = issue;
        this.pages = pages;
        this.publisher = publisher;
        this.description = description;
        this.citedIn = citedIn;
    }
}
