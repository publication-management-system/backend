package com.pms.publicationmanagement.dto;

import java.util.List;

public class DblpArticleDto {

    public List<String> author;

    public String title;

    public String pages;

    public String year;

    public String volume;

    public String journal;

    public List<String> ee;

    public String url;

    public DblpArticleDto() {
    }


    public DblpArticleDto(List<String> author, String title, String pages, String year, String volume, String journal, List<String> links, String url) {
        this.author = author;
        this.title = title;
        this.pages = pages;
        this.year = year;
        this.volume = volume;
        this.journal = journal;
        this.ee = links;
        this.url = url;
    }
}
