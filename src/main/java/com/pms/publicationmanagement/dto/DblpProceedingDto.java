package com.pms.publicationmanagement.dto;

import java.util.List;

public class DblpProceedingDto {

    public List<String> editor;

    public String title;

    public String booktitle;

    public String publisher;

    public String year;

    public String series;

    public String volume;

    public String ee;

    public DblpProceedingDto() {
    }

    public DblpProceedingDto(List<String> editor, String title, String booktitle, String publisher, String year, String series, String volume, String ee) {
        this.editor = editor;
        this.title = title;
        this.booktitle = booktitle;
        this.publisher = publisher;
        this.year = year;
        this.series = series;
        this.volume = volume;
        this.ee = ee;
    }
}
