package com.pms.publicationmanagement.service.scraping.scopus;

import com.google.gson.Gson;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.pms.publicationmanagement.model.scraping.DataSourceType;
import com.pms.publicationmanagement.model.scraping.ScrapedEntity;
import com.pms.publicationmanagement.model.scraping.ScrapedEntityType;
import com.pms.publicationmanagement.model.scraping.ScrapingSession;
import com.pms.publicationmanagement.model.scraping.payloads.AuthorDocumentsPayload;
import com.pms.publicationmanagement.repository.ScrapedEntityRepository;
import com.pms.publicationmanagement.service.scraping.IWebScrapingStep;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.pms.publicationmanagement.service.scraping.scopus.ScopusWebScraperService.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScopusDocumentsScraping implements IWebScrapingStep {

    private final ScrapedEntityRepository scrapedEntityRepository;

    public static final String PUBLICATION_YEAR_SPAN = "span.Typography-module__lVnit";

    @Override
    public void scrapeEntity(Page page, ScrapingSession scrapingSession, UUID parentId) {

        getToAuthorProfile(page, scrapingSession);

        List<AuthorDocumentsPayload> payload = scrapeDocuments(page);

        saveScrapedEntity(payload, parentId, null);


    }

    private static void getToAuthorProfile(Page page, ScrapingSession scrapingSession) {
        page.navigate(SCOPUS_HOME_PAGE);

        page.locator(AUTHOR_SEARCH_ICON).click();
        page.getByLabel("Author last name").fill(scrapingSession.getLastName());
        page.getByLabel("Author first name").fill(scrapingSession.getFirstName());
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Search")).first().click();
        page.locator(AUTHOR_SEARCH_FIRST_RESULT).locator("td").locator("a").click();
    }

    private List<AuthorDocumentsPayload> scrapeDocuments(Page page) {

        page.locator(DOCUMENTS_CONTAINER_ID).scrollIntoViewIfNeeded();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        List<AuthorDocumentsPayload> authorDocuments = new ArrayList<>();

        List<Locator> docLocators = page.getByTestId("results-list-item").all();
        for(Locator l : docLocators) {
            AuthorDocumentsPayload publication = new AuthorDocumentsPayload();

            publication.setTitle(l.locator("h4").innerText());

            List<String> authorNames = l.getByTestId("author-list").locator("span").
                    locator("a").locator("span").allInnerTexts();

            publication.setCoAuthorsNames(authorNames);

            publication.setIssued(l.locator("em").last().innerText());

//            Locator child = l.locator("span.Typography-module__lVnit").locator("em");
//
//            Locator parent = l.locator("span").filter(new Locator.FilterOptions().setHas(child));

            Locator dateLocator = l.locator("span.Typography-module__lVnit .Typography-module__Nfgvc").all().get(1);

            publication.setPublicationDate(dateLocator.innerText().split(",")[0]);

            authorDocuments.add(publication);
        }
        return authorDocuments;
    }

    private void saveScrapedEntity(List<AuthorDocumentsPayload> authorDocumentsPayloads, UUID parentId, UUID sessionId) {

        ScrapedEntity scrapedEntity = new ScrapedEntity();
        scrapedEntity.setParentId(parentId);
        scrapedEntity.setSessionId(sessionId);
        scrapedEntity.setPayload(new Gson().toJson(authorDocumentsPayloads));
        scrapedEntity.setType(ScrapedEntityType.DOCUMENT);
        scrapedEntity.setDataSource(DataSourceType.SCOPUS);

        scrapedEntityRepository.save(scrapedEntity);
    }
}
