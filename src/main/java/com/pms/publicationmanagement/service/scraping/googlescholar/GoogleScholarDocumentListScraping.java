package com.pms.publicationmanagement.service.scraping.googlescholar;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.pms.publicationmanagement.model.scraping.ScrapingSession;
import com.pms.publicationmanagement.service.scraping.IWebScrapingStep;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleScholarDocumentListScraping implements IWebScrapingStep {

    public static final String SHOW_MORE_BUTTON_ID = "#gsc_bpf_more";
    public static final String DOCUMENTS_TABLE = "#gsc_a_b";
    public static final String TABLE_ROW = "tr";
    public static final String LINK = "a";
    public static final String GOOGLE_SCHOLAR_BASE_URL = "https://scholar.google.com/%s";
    public static final String HREF_ATTRIBUTE = "href";
    public static final String TABLE_DATA = "td";

    private final GoogleScholarSingleDocumentScraping singleDocumentScraping;

    @Override
    public void scrapeEntity(Page page, ScrapingSession scrapingSession, UUID parentId) {
        log.info("Sraping google scholar document");
        List<String> documentLinks = new ArrayList<>();
        clickOnShowMoreUntilNoMore(page);
        int documentCount = page.locator(DOCUMENTS_TABLE).locator(TABLE_ROW).count();
        for (int currentDocumentIndex = 0; currentDocumentIndex < documentCount; ++currentDocumentIndex) {
            Locator documentLink = page.locator(DOCUMENTS_TABLE).locator(TABLE_ROW).all().get(currentDocumentIndex)
                    .locator(TABLE_DATA).first().locator(LINK);

            documentLinks.add(String.format(GOOGLE_SCHOLAR_BASE_URL, documentLink.getAttribute(HREF_ATTRIBUTE)));
        }
        log.info("Document links found: {}", documentLinks);

        for (String documentLink : documentLinks) {
            page.navigate(documentLink);
            singleDocumentScraping.scrapeEntity(page, scrapingSession, parentId);
        }
    }

    private void clickOnShowMoreUntilNoMore(Page page) {
        Locator showMore = page.locator(SHOW_MORE_BUTTON_ID);
        while (showMore.isVisible() && showMore.isEnabled()) {
            showMore.click();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        log.info("TEst");
    }
}