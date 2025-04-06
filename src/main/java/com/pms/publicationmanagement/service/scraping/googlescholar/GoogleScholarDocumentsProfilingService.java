package com.pms.publicationmanagement.service.scraping.googlescholar;

import com.microsoft.playwright.Page;
import com.pms.publicationmanagement.model.scraping.ScrapingSession;
import com.pms.publicationmanagement.service.scraping.IWebScrapingStep;

import java.util.UUID;

public class GoogleScholarDocumentsProfilingService implements IWebScrapingStep {


    @Override
    public void scrapeEntity(Page page, ScrapingSession scrapingSession, UUID parentId) {

    }
}
