package com.pms.publicationmanagement.model.profiling;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    private String firstName;

    private String lastName;

    private String middleName;

    private String googleScholarId;

    private String dblpId;

    private String wosId;

    private String institutionRole;

    private String institution;

    private String institutionMail;

    private String internalRefId;

    private String imageUrl;

    private String topics;

    @ManyToMany
    private List<Document> documents = new ArrayList<>();

    public void addDocuments(List<Document> documents) {
        this.documents.addAll(documents);
    }
}
