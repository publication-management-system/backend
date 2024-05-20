package com.pms.publicationmanagement.services.scraping.dblp;

import com.pms.publicationmanagement.dto.DblpArticleDto;
import com.pms.publicationmanagement.dto.DblpInproceedingsDto;
import com.pms.publicationmanagement.dto.DblpProceedingDto;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Dblp {

    private DblpArticleDto article;

    private DblpInproceedingsDto inproceedings;

    private DblpProceedingDto proceedings;

    public Dblp(DblpProceedingDto proceedings) {
        this.proceedings = proceedings;
    }

    public Dblp(DblpArticleDto article) {
        this.article = article;
    }

    public Dblp(DblpInproceedingsDto conferencePaper) {
        this.inproceedings = conferencePaper;
    }

    public DblpInproceedingsDto getInproceedings() {
        return inproceedings;
    }

    public void setInproceedings(DblpInproceedingsDto inproceedings) {
        this.inproceedings = inproceedings;
    }

    public DblpArticleDto getArticle() {
        return article;
    }

    public void setArticle(DblpArticleDto article) {
        this.article = article;
    }

    public DblpProceedingDto getProceedings() {
        return proceedings;
    }

    public void setProceedings(DblpProceedingDto proceedings) {
        this.proceedings = proceedings;
    }

    public Dblp() {
    }
}
