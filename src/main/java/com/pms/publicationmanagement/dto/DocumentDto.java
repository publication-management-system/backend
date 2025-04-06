package com.pms.publicationmanagement.dto;

import com.pms.publicationmanagement.model.profiling.Author;
import com.pms.publicationmanagement.model.profiling.Citation;

import java.util.List;
import java.util.UUID;

public class DocumentDto {
    public UUID id;
    public String title;
    public String publicationDate;
    public List<Author> authors;
    public String issued;
    public String volume;
    public String issue;
    public String pages;
    public String publisher;
    public String description;
    public String link;
    public List<Citation> citedIn;
}
