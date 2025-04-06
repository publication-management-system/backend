package com.pms.publicationmanagement.service.scraping.googlescholar;

import com.google.gson.Gson;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.pms.publicationmanagement.model.scraping.DataSourceType;
import com.pms.publicationmanagement.model.scraping.ScrapedEntity;
import com.pms.publicationmanagement.model.scraping.ScrapedEntityType;
import com.pms.publicationmanagement.model.scraping.ScrapingSession;
import com.pms.publicationmanagement.model.scraping.payloads.AuthorCitationsPayload;
import com.pms.publicationmanagement.repository.ScrapedEntityRepository;
import com.pms.publicationmanagement.service.scraping.IWebScrapingStep;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.pms.publicationmanagement.service.scraping.googlescholar.GoogleScholarWebScraperService.*;
import static com.pms.publicationmanagement.service.scraping.googlescholar.locators.GoogleScholarLocators.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleScholarCitationsScraping implements IWebScrapingStep {


    private final ScrapedEntityRepository scrapedEntityRepository;
    @Override
    public void scrapeEntity(Page page, ScrapingSession scrapingSession, UUID parentId) {
        List<Locator> docDetailsLocator = page.locator(DOCUMENT_DETAILS_TABLE).locator(DOCUMENT_DETAILS_TABLE_ROW).locator(DOCUMENT_DETAILS_TABLE_VALUE).all();
        Locator citations = docDetailsLocator.get(docDetailsLocator.size() - 2).locator(ANCHOR_TAG).all().get(0);

        List<AuthorCitationsPayload> payloads = extractCitations(page, citations);

        saveScrapedEntity(payloads, parentId, scrapingSession.getId());
    }

    private List<AuthorCitationsPayload> extractCitations(Page page, Locator firstLink) {
        log.info("Extracting citation");
        String docDetailUrl = page.url();
        firstLink.click();

        List<AuthorCitationsPayload> citationsPayload = new ArrayList<>();
        addPageOfCitations(citationsPayload, page);

        List<Locator> pageColumns = page.locator(CITATIONS_PAGINATION).locator(TABLE_ROW).locator(TABLE_COLUMN)
                .all();
        Locator nextPage = pageColumns.size() > 0 ? pageColumns.get(pageColumns.size() - 1) : null;

        while (nextPage != null && nextPage.locator("b").isVisible()) {
            nextPage.click();
            addPageOfCitations(citationsPayload, page);
        }

        log.info("citationsPayload {}", citationsPayload);

        page.navigate(docDetailUrl);
        return citationsPayload;

//        scrapedDocument.setCitedIn(citationList);
    }

    private void addPageOfCitations(List<AuthorCitationsPayload> citationList, Page citationPage) {
        citationPage.waitForSelector(CITATIONS_CONTAINER_ID);
        List<Locator> citations = citationPage.locator(CITATIONS_CONTAINER_ID).locator(CITATION_ROW).all();
        for (Locator citation : citations) {
            String citationTitle, citationLink;
            Locator citationHeader = citation.locator(HEADER_3);
            Locator citationDetails = citationHeader.locator(ANCHOR_TAG);
            if (citationDetails.all().isEmpty()) {
                citationTitle = citationHeader.locator(SPAN).all().get(3).innerText(); //doamne ajuta
                citationLink = citation.locator(CITATION_UNAVAILABLE_AUTHOR).innerText(); //sunt defapt autori in cazul asta
            } else {
                citationTitle = citationDetails.innerText();
                citationLink = citationDetails.getAttribute(HREF_ATTRIBUTE);
            }
            AuthorCitationsPayload citationToBeAdded = new AuthorCitationsPayload();
            citationToBeAdded.setLink(citationLink);
            citationToBeAdded.setTitle(citationTitle);
            citationList.add(citationToBeAdded);
        }
    }

    private void saveScrapedEntity(List<AuthorCitationsPayload> authorCitationsPayload, UUID parentId, UUID sessionId) {
        List<ScrapedEntity> entities = new ArrayList<>();
        for(AuthorCitationsPayload payload : authorCitationsPayload) {
            ScrapedEntity scrapedEntity = new ScrapedEntity();

            scrapedEntity.setParentId(parentId);
            scrapedEntity.setSessionId(sessionId);
            scrapedEntity.setPayload(new Gson().toJson(payload));
            scrapedEntity.setType(ScrapedEntityType.CITATION);
            scrapedEntity.setDataSource(DataSourceType.GOOGLE_SCHOLAR);
            entities.add(scrapedEntity);
        }

        scrapedEntityRepository.saveAll(entities);
    }
}
