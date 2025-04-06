package com.pms.publicationmanagement.model.profiling;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
public class Citation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String title;

    private String link;

    @ManyToOne
    @JoinColumn(name="document_id")
    private Document document;

    public Citation(UUID id, String title, String link, Document document) {
        this.id = id;
        this.title = title;
        this.link = link;
        this.document = document;
    }

    public Citation() {
    }
}
