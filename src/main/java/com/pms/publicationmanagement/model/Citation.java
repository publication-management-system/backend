package com.pms.publicationmanagement.model;

import jakarta.persistence.*;

@Entity
public class Citation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    private String link;

    @ManyToOne
    @JoinColumn(name="document_id")
    private Document document;

    public Citation(Integer id, String title, String link, Document document) {
        this.id = id;
        this.title = title;
        this.link = link;
        this.document = document;
    }

    public Citation() {
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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }
}
