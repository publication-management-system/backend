package com.pms.publicationmanagement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String role;

    private String institution;

    private String institutionMail;

    @ManyToMany
    private List<Document> documents = new ArrayList<>();

    public void addDocuments(List<Document> documents) {
        this.documents.addAll(documents);
    }
}
