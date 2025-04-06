package com.pms.publicationmanagement.service.scraping.scopus;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.pms.publicationmanagement.config.scraping.ScrapingConfig;
import com.pms.publicationmanagement.model.scraping.ScrapingSession;
import com.pms.publicationmanagement.service.scraping.IWebScrapingProfiling;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ScopusService implements IWebScrapingProfiling {

    private final ScrapingConfig scrapingConfig;

    private final ScopusDocumentsScraping scopusDocumentsScraping;
    @Override
    public void scrape(ScrapingSession scrapingSession) {
        List<String> args = new ArrayList<>();
        args.add("--private");
        try (Playwright playwright = Playwright.create();
             Browser browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(false).setArgs(args))) {

            Page page = browser.newPage();
            scopusDocumentsScraping.scrapeEntity(page, scrapingSession, null);

        } catch (RuntimeException ex) {
            log.error("Error scraping google scholar data", ex);
        }
    }
}
