package com.pms.publicationmanagement.service.scraping.googlescholar;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.pms.publicationmanagement.model.scraping.ScrapingSession;
import com.pms.publicationmanagement.service.scraping.IWebScrapingStep;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.pms.publicationmanagement.service.scraping.googlescholar.locators.GoogleScholarLocators.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleScholarSingleDocumentScraping implements IWebScrapingStep {

    public static final String DOCUMENT_TITLE = "#gsc_oci_title_wrapper";
    public static final String FIELD = "[class=gsc_oci_field]";
    public static final String VALUE = "[class=gsc_oci_value]";

    private final GoogleScholarCitationsScraping citationScrapingStep;

    @Override
    public void scrapeEntity(Page page, ScrapingSession scrapingSession, UUID parentId) {
        log.info("scrapeEntity");
        try {
            page.waitForSelector(DOCUMENT_TITLE, new Page.WaitForSelectorOptions().setTimeout(3000));
            Map<String, String> fields = getDocumentPageInfoMap(page);
            List<Locator> docDetailsLocator = page.locator(DOCUMENT_DETAILS_TABLE).locator(DOCUMENT_DETAILS_TABLE_ROW).locator(DOCUMENT_DETAILS_TABLE_VALUE).all();
            if ( fields.get("total_citations") != null )
            {
                citationScrapingStep.scrapeEntity(page, scrapingSession, parentId);
            }
            log.info("Scraped document {}", page.locator("div#gsc_oci_title").innerText());
            int min_wait = 1000;
            int max_wait = 2000;
            int wait_time = (int) ((Math.random() * (max_wait - min_wait)) + min_wait);
            try {
                Thread.sleep(wait_time);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }


        } catch (Exception e) {
            log.error("Could not get document", e);
        }
    }

    private Map<String, String> getDocumentPageInfoMap(Page page) {
        Map<String, String> documentPageInfoMap = new HashMap<>();

        List<Locator> keyValueLocators = page.locator("#gsc_oci_table").locator("[class=gs_scl]").all();
        for (Locator keyValueLocator : keyValueLocators) {
            documentPageInfoMap.put(keyValueLocator.locator(FIELD).innerText().trim().replace(" ", "_")
                    .toLowerCase(), keyValueLocator.locator(VALUE).textContent());
        }


        return documentPageInfoMap;
    }

}