package com.pms.publicationmanagement.model.profiling;

import jakarta.persistence.*;

import java.io.Serializable;

@Embeddable
public class DocumentOwnership implements Serializable {

    @EmbeddedId
    DocumentOwnership id;

    @ManyToOne
    @MapsId("authorId")
    @JoinColumn(name = "author_id")
    Author author;

    @ManyToOne
    @MapsId("documentId")
    @JoinColumn(name = "document_id")
    Document document;

}
