package com.pms.publicationmanagement.controllers;

import com.pms.publicationmanagement.services.scraping.dblp.DblpWebScraperService;
import com.pms.publicationmanagement.services.scraping.googlescholar.GoogleScholarWebScraperService;
import com.pms.publicationmanagement.services.scraping.scopus.ScopusWebScraperService;
import com.pms.publicationmanagement.services.scraping.webofscience.WebOfScienceScraperService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/web-scraping")
public class WebScrapingController {

    private final GoogleScholarWebScraperService webScraper;

    private final DblpWebScraperService dblpWebScraper;

    private final WebOfScienceScraperService webOfScienceWebScrapper;

    private final ScopusWebScraperService scopusWebScraper;

    public WebScrapingController(GoogleScholarWebScraperService webScraper, DblpWebScraperService dblpWebScraper,
                                 WebOfScienceScraperService webOfScienceWebScrapper, ScopusWebScraperService scopusWebScraper) {
        this.webScraper = webScraper;
        this.dblpWebScraper = dblpWebScraper;
        this.webOfScienceWebScrapper = webOfScienceWebScrapper;
        this.scopusWebScraper = scopusWebScraper;
    }

    @PostMapping("/google-scholar")
    public String scrapeGoogleScholar(@RequestParam String name) {
//        webScraper.scrape(name);
//        dblpWebScraper.scrape(name);
        String nameCopy = name;
        String names[] = nameCopy.split(" ");
        webOfScienceWebScrapper.scrape(names[0], names[1]);
//        scopusWebScraper.scrape(names[0], names[1]);
        return "OK";
    }

}