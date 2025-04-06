package com.pms.publicationmanagement.service.scraping.webofscience;

import com.google.gson.Gson;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
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
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class WosDocumentsScraping implements IWebScrapingStep {

    private final ScrapedEntityRepository scrapedEntityRepository;

    @Override
    public void scrapeEntity(Page page, ScrapingSession scrapingSession, UUID parentId) {

        authenticate(page);
        List<AuthorDocumentsPayload> docs =  getToAuthorProfile(scrapingSession.getLastName(),
                scrapingSession.getFirstName(), page);
        saveScrapedEntity(docs, parentId, null);
    }

    private void authenticate(Page page) {
        page.navigate("https://access.clarivate.com/login?app=wos&alternative=true&shibShireURL=https:%2F%2Fwww.webofknowledge.com%2F%3Fauth%3DShibboleth&shibReturnURL=https:%2F%2Fwww.webofknowledge.com%2F&roaming=true");
        page.locator("#mat-input-0").fill("ginjucristi@gmail.com");
        page.locator("#mat-input-1").fill("Test123*");
        page.locator("#signIn-btn").click();
    }

    private List<AuthorDocumentsPayload> getToAuthorProfile(String lastName, String firstName, Page page) {
//        page.waitForSelector("app-author-search");
        page.locator("#onetrust-reject-all-handler").click();
        page.locator("#mat-input-0").fill(lastName);
        page.locator("#mat-input-1").fill(firstName);
        page.locator("div.button-row").locator("button").last().click();
//        page.waitForSelector("div.results-column");
        List<AuthorDocumentsPayload> documentsPayloads = new ArrayList<>();
        getPageOfPublications(page, documentsPayloads);
        Locator nextPageButton = page.locator("form.pagination").locator("button").last();

        while (nextPageButton.isEnabled()) {
            nextPageButton.click();
            getPageOfPublications(page, documentsPayloads);
            nextPageButton = nextPageButton = page.locator("form.pagination").locator("button").last();;
        }
        return documentsPayloads;
    }
    private void getPageOfPublications(Page page, List<AuthorDocumentsPayload> documentsPayloads) {
        page.waitForSelector("app-publications-tab"); //trebuie un wait mai bun
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        List<Locator> publicationsLocators = page.locator("app-publications-tab").locator("app-record").all();
        for(Locator l : publicationsLocators) {
            AuthorDocumentsPayload docPayload = new AuthorDocumentsPayload();
            l.scrollIntoViewIfNeeded();
            l.locator("app-summary-title").locator("a").click();
            page.waitForSelector("#snMainArticle");
            String[] authors = page.locator("#SumAuthTa-MainDiv-author-en").locator("span.cdx-grid-data").innerText().split(";");
            List<String> listOfSavedAuthors = new ArrayList<>();
            listOfSavedAuthors.addAll(Arrays.asList(authors));
            docPayload.setCoAuthorsNames(listOfSavedAuthors);

            docPayload.setTitle(page.locator("#FullRTa-fullRecordtitle-0").innerText());

            String source = page.locator("mat-sidenav-content").first().innerText();
            docPayload.setIssued(source);

            String publicationDate = page.locator("#FullRTa-pubdate").innerText();
            docPayload.setPublicationDate(publicationDate);

            if(!page.locator("#FullRTa-volume").all().isEmpty()) {
                docPayload.setVolume(page.locator("#FullRTa-volume").innerText());
            }

            if(!page.locator("#FullRTa-abstract-basic").all().isEmpty()) {
                docPayload.setDescription(page.locator("#FullRTa-abstract-basic").innerText());
            }

            if(!page.locator("#FullRTa-issue").all().isEmpty()) {
                docPayload.setIssue(page.locator("#FullRTa-issue").innerText());
            }

            if(!page.locator("#FullRTa-pageNo").all().isEmpty()) {
                docPayload.setPages(page.locator("#FullRTa-pageNo").innerText());
            }

            documentsPayloads.add(docPayload);

//            https://dx.doi.org/
//            String link = page.locator("#FullRTa-DOI").innerText(); //se poate gasi link din asta(nu stiu daca trebuie)

            page.goBack();

            page.waitForSelector("app-publications-tab"); //trebuie un wait mai bun
//            springJpaDocumentRepository.save(publication);
        }
    }

    private void saveScrapedEntity(List<AuthorDocumentsPayload> authorDocumentsPayloads, UUID parentId, UUID sessionId) {

        ScrapedEntity scrapedEntity = new ScrapedEntity();
        scrapedEntity.setParentId(parentId);
        scrapedEntity.setSessionId(sessionId);
        scrapedEntity.setPayload(new Gson().toJson(authorDocumentsPayloads));
        scrapedEntity.setType(ScrapedEntityType.DOCUMENT);
        scrapedEntity.setDataSource(DataSourceType.WEB_OF_SCIENCE);
        scrapedEntityRepository.save(scrapedEntity);
    }

}
