package com.pms.publicationmanagement.model.profiling;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Citation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String title;

    @Lob
    @Column(length = 1000)
    private String link;

    @Lob
    @Column(length = 1000)
    private String pdf;

    @ManyToOne
    @JoinColumn(name="document_id")
    private Document document;
}
