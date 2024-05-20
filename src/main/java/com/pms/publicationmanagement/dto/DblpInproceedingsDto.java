package com.pms.publicationmanagement.dto;

import java.util.List;

public class DblpInproceedingsDto {

    public List<String> author;

    public String title;

    public String pages;

    public String year;

    public String booktitle;

    public List<String> ee;

    public String crossref;

    public String url;

    public DblpInproceedingsDto() {
    }

    public DblpInproceedingsDto(List<String> author, String title, String pages, String year, String booktitle, List<String> ee, String crossref, String url) {
        this.author = author;
        this.title = title;
        this.pages = pages;
        this.year = year;
        this.booktitle = booktitle;
        this.ee = ee;
        this.crossref = crossref;
        this.url = url;
    }
}
