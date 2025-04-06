package com.pms.publicationmanagement.service.scraping;

import com.microsoft.playwright.Page;
import com.pms.publicationmanagement.model.scraping.ScrapingSession;

import java.util.UUID;

public interface IWebScrapingStep {
    void scrapeEntity(Page page, ScrapingSession scrapingSession, UUID parentId);
}
