package com.pms.publicationmanagement.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
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
}
