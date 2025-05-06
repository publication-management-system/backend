package com.pms.publicationmanagement.service.scraping.dblp;

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

//import static com.pms.publicationmanagement.service.scraping.dblp.DblpDocumentsScraping.extractXmlObject;

@Service
@RequiredArgsConstructor
@Slf4j
public class DblpCitationScraping implements IWebScrapingStep {

    private final ScrapedEntityRepository scrapedEntityRepository;

    private final DblpDocumentsScraping dblpDocumentsScraping;

    @Override
    public void scrapeEntity(Page page, ScrapingSession scrapingSession, UUID parentId) {

        List<Locator> docCitationList = page.locator("a").getByText("references & citations").all();

        List<AuthorCitationsPayload> payload = new ArrayList<>();

        for(Locator l : docCitationList) {
            extractCitations(payload, page, l.getAttribute("href"));
        }

        saveScrapedEntity(payload, parentId, scrapingSession.getId());
    }

    private void extractCitations(List<AuthorCitationsPayload> payload, Page page, String citationsPage) {
        page.navigate(citationsPage);
        page.waitForSelector("#references-load");

        if(!page.locator("#references-load").locator("input").isChecked()) {
            page.locator("#references-load").locator("a").click();
        }

        page.waitForSelector("#publ-references-section");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        List<Locator> citationsWithoutLink = page.locator("#publ-references-section").locator("li:has(img.no-match)").all();
        List<Locator> citations = page.locator("#publ-references-section").locator("li:has(nav.publ)").all();

        for(Locator l : citations) {
            AuthorCitationsPayload toBeAdded = new AuthorCitationsPayload();
            toBeAdded.setTitle(l.locator("cite:has(span.title)").locator("span.title").innerText());
            String citationXml = l.locator("nav").locator("ul").locator("li.drop-down")
                    .all().get(1).locator("div.body").locator("ul").first().locator("li")
                    .last().locator("a").getAttribute("href");
            Dblp citationDetails = dblpDocumentsScraping.extractXmlObject(citationXml);
            if(citationDetails.getProceedings() != null) {
                toBeAdded.setLink(citationDetails.getProceedings().ee);
            }
            else if(citationDetails.getInproceedings() != null) {
                toBeAdded.setLink(citationDetails.getInproceedings().ee.get(0));
            }
            else if(citationDetails.getArticle() != null) {
                toBeAdded.setLink(citationDetails.getArticle().ee.get(0));
            }

            payload.add(toBeAdded);
            log.info("Scraping citation {}", toBeAdded.getTitle());
        }

        for(Locator l : citationsWithoutLink) {
            AuthorCitationsPayload toBeAdded = new AuthorCitationsPayload();
            toBeAdded.setTitle(l.locator("cite:has(span.title)").locator("span.title").innerText());
            toBeAdded.setLink("No link found");
            payload.add(toBeAdded);
        }
        page.goBack();

    }

    private void saveScrapedEntity(List<AuthorCitationsPayload> authorCitationsPayload, UUID parentId, UUID sessionId) {

        List<ScrapedEntity> entities = new ArrayList<>();
        for(AuthorCitationsPayload payload : authorCitationsPayload) {
            ScrapedEntity scrapedEntity = new ScrapedEntity();
            scrapedEntity.setParentId(parentId);
            scrapedEntity.setSessionId(sessionId);
            scrapedEntity.setPayload(new Gson().toJson(authorCitationsPayload));
            scrapedEntity.setType(ScrapedEntityType.CITATION);
            scrapedEntity.setDataSource(DataSourceType.DBLP);
            entities.add(scrapedEntity);
        }

        scrapedEntityRepository.saveAll(entities);
    }
}
