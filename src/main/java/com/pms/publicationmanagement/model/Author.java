package com.pms.publicationmanagement.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Author {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String role;

    private String institution;

    private String institutionMail;

//    @ElementCollection
//    private List<String> topics;

    @ManyToMany
    private List<Document> documents = new ArrayList<>();

    public Author() {
    }

    public Author(Integer id, String name, String role, String institution, String institutionMail, List<Document> documents) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.institution = institution;
        this.institutionMail = institutionMail;
        this.documents = new ArrayList<>();;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String title) {
        this.role = title;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getInstitutionMail() {
        return institutionMail;
    }

    public void setInstitutionMail(String institutionMail) {
        this.institutionMail = institutionMail;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void addDocuments(List<Document> documents) {
        this.documents.addAll(documents);
    }

    //    public List<String> getLabels() {
//        return topics;
//    }
//
//    public void setLabels(List<String> labels) {
//        this.topics = labels;
//    }
}
