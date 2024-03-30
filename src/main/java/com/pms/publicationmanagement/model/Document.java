package com.pms.publicationmanagement.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;


/*
2 tipuri de documente: jurnal si conferinta(in functie de unde au fost publicate)
date despre jurnal/conferinta:
    https://uefiscdi.gov.ro/scientometrie-baze-de-date,
    http://portal.core.edu.au/conf-ranks/
 */
@Entity
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String publicationDate;
    @ManyToMany
    private List<Author> authors;
    private String issued;
    private String volume;
    private String issue;
    private String pages;
    private String publisher;
    private String description;

    private String link;

//    @ElementCollection
//    private List<String> topics;

    @OneToMany(fetch = FetchType.LAZY, mappedBy="document", cascade = CascadeType.ALL)
    private List<Citation> citedIn;

//    @OneToMany(fetch = FetchType.LAZY, mappedBy="document", cascade = CascadeType.ALL)
//    private List<Document> referencedIn;


    public Document(Integer id, String title, String publicationDate, List<Author> authors, String issued,
                    String volume, String issue, String pages, String publisher, String description, List<Citation> citedIn) {
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

    public Document(Integer id, String title, String publicationDate, List<Author> authors, String issued, String volume, String issue, String pages, String publisher, String description, String link, List<Citation> citedIn) {
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
        this.link = link;
        this.citedIn = citedIn;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Document() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    public String getIssued() {
        return issued;
    }

    public void setIssued(String issued) {
        this.issued = issued;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Citation> getCitedIn() {
        return citedIn;
    }

    public void setCitedIn(List<Citation> citedIn) {
        this.citedIn = citedIn;
    }
}
