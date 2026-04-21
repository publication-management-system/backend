package com.pms.publicationmanagement.model.profiling;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;


/*
2 tipuri de documente: jurnal si conferinta(in functie de unde au fost publicate)
date despre jurnal/conferinta:
    https://uefiscdi.gov.ro/scientometrie-baze-de-date,
    http://portal.core.edu.au/conf-ranks/
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String title;
    private String publicationDate;
    @ManyToMany
    private List<Author> authors;

    private String issued;

    @Lob
    @Column(length = 1000)
    private String volume;

    @Lob
    @Column(length = 1000)
    private String issue;

    private String pages;

    @Lob
    @Column(length = 1000)
    private String publisher;

    @Lob
    @Column(length = 16777216)
    private String description;

    @Lob
    @Column(length = 1000)
    private String link;

    @OneToMany(fetch = FetchType.LAZY, mappedBy="document", cascade = CascadeType.ALL)
    private List<Citation> citedIn;

    private String googleScholarId;
    private String dblpId;
    private String wosId;

    private String internalRefId;
}
