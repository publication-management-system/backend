package com.pms.publicationmanagement.dto;

import com.pms.publicationmanagement.model.Author;
import com.pms.publicationmanagement.model.Citation;

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
    public String link;
    public List<Citation> citedIn;
}
