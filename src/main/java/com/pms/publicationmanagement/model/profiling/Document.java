package com.pms.publicationmanagement.model.profiling;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String title;
    private String publicationDate;
    @ManyToMany
    private List<Author> authors;
    private String issued;
    private String volume;
    private String issue;
    private String pages;
    private String publisher;
    private String description;
    private String link;
    @OneToMany(fetch = FetchType.LAZY, mappedBy="document", cascade = CascadeType.ALL)
    private List<Citation> citedIn;
}
