package com.pms.publicationmanagement.controllers;

import com.pms.publicationmanagement.services.GoogleScholarWebScraperService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/web-scraping")
public class WebScrapingController {

    private final GoogleScholarWebScraperService webScraper;

    public WebScrapingController(GoogleScholarWebScraperService webScraper) {
        this.webScraper = webScraper;
    }

    @PostMapping("/google-scholar")
    public String scrapeGoogleScholar(@RequestParam String name) {
        webScraper.scrape(name);
        return "OK";
    }

}