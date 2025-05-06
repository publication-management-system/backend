package com.pms.publicationmanagement.service.scraping.dblp;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.pms.publicationmanagement.config.scraping.ScrapingConfig;
import com.pms.publicationmanagement.model.scraping.ScrapingSession;
import com.pms.publicationmanagement.service.scraping.IWebScrapingProfiling;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jobrunr.jobs.annotations.Job;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class DblpService  implements IWebScrapingProfiling  {

    private final ScrapingConfig scrapingConfig;

    private final DblpDocumentsScraping dblpDocumentsScraping;

    private final DblpCitationScraping dblpCitationScraping;
    @Override
    @Job(name = "dblp-scholar-scraping")
    public void scrape(ScrapingSession scrapingSession) {
        try (Playwright playwright = Playwright.create();

             Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(!scrapingConfig.isShowBrowser()).setArgs(scrapingConfig.getBrowserArgs()))) {
            Page page = browser.newPage();
            dblpDocumentsScraping.scrapeEntity(page, scrapingSession, null);
            dblpCitationScraping.scrapeEntity(page, scrapingSession, null); 
        } catch (RuntimeException ex) {
            log.error("Error scraping google scholar data", ex);
        }

    }
}
