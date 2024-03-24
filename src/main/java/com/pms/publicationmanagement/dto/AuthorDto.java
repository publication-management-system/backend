package com.pms.publicationmanagement.dto;

import com.pms.publicationmanagement.model.Document;

import java.util.List;

public class AuthorDto {

    public Integer id;

    public String name;

    public String role;

    public String institution;

    public String institutionMail;

    public List<Document> documents;

    public AuthorDto() {
    }

    public AuthorDto(Integer id, String name, String role, String institution, String institutionMail, List<Document> documents) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.institution = institution;
        this.institutionMail = institutionMail;
        this.documents = documents;
    }
}
