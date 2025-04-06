package com.pms.publicationmanagement.service.scraping.googlescholar;

import com.google.gson.Gson;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.pms.publicationmanagement.model.profiling.Document;
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

import static com.pms.publicationmanagement.service.scraping.googlescholar.locators.GoogleScholarLocators.*;
import static com.pms.publicationmanagement.service.scraping.googlescholar.locators.GoogleScholarLocators.DISABLED;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleScholarDocumentsScraping implements IWebScrapingStep {

    private final ScrapedEntityRepository scrapedEntityRepository;

    private final GoogleScholarCitationsScraping citationsScraping;

    @Override
    public void scrapeEntity(Page page, ScrapingSession scrapingSession, UUID parentId) {

        log.info("GOOGLE_SCHOLAR: Searching for {} {}", scrapingSession.getFirstName(), scrapingSession.getLastName());
//        String name = String.format("%s %s", scrapingSession.getFirstName(), scrapingSession.getLastName());

        Locator showMoreButton = page.locator(SHOW_MORE_BUTTON_ID);
        List<String> docLinks = new ArrayList<>();
        List<String> docTitles = new ArrayList<>();
        AuthorDocumentsPayload payload = new AuthorDocumentsPayload();

        clickOnShowMoreUntilNoMore(page, showMoreButton, docLinks, docTitles);
        scrapeDocuments(page, docLinks, docTitles, payload);

        ScrapedEntity document = saveScrapedEntity(payload, parentId, scrapingSession.getId());

        citationsScraping.scrapeEntity(page, scrapingSession, document.getId());
    }

    private void scrapeDocuments(Page page, List<String> docLinks, List<String> docTitles, AuthorDocumentsPayload payload) {
        for (int currentDoc = 0; currentDoc < docLinks.size(); currentDoc++) {
//            if (!springJpaDocumentRepository.findByTitle(docTitles.get(currentDoc)).isEmpty()) {
//                System.out.println("Did not find doc by title"); // nu inseamna ca am gasit documentul? not .isEmpty
//                continue;
//            }
            //nu mai trebuie?

            Document scrapedDocument = new Document();
            scrapedDocument.setTitle(docTitles.get(currentDoc));

            page.navigate(docLinks.get(currentDoc));
            page.waitForSelector(DOCUMENT_DETAILS_TABLE);
            Locator documentDetailsTableRow = page.locator(DOCUMENT_DETAILS_TABLE).locator(DOCUMENT_DETAILS_TABLE_ROW);
            List<Locator> docDetails = documentDetailsTableRow.locator(DOCUMENT_DETAILS_TABLE_FIELD).all();
            List<Locator> docInfo = documentDetailsTableRow.locator(DOCUMENT_DETAILS_TABLE_VALUE).all();

            for (int i = 0; i < docDetails.size(); i++) {
                scrapeDetail(page, scrapedDocument, docDetails.get(i).innerText(), docInfo.get(i));
            }
        }

    }

    private void clickOnShowMoreUntilNoMore (Page page, Locator
            showMoreButton, List <String>docLinks, List < String > docTitles) {
        int currentDocIndex = 0;
        do {
            if (showMoreButton.getAttribute(DISABLED) == null) {
                showMoreButton.click();
            }

            List<Locator> documents = page.locator(DOCUMENTS_CONTAINER_ID).locator(TABLE_BODY)
                    .locator(TABLE_ROW).all();

            int numberOfDocs = documents.size();

            for (int index = currentDocIndex; index < numberOfDocs; ++index) {
                Locator documentTitleLocator = documents.get(index)
                        .locator(DOCUMENT_TITLE_CLASS).all().get(0).locator(ANCHOR_TAG);

                docLinks.add(
                        String.format(GOOGLE_SCHOLAR_BASE_URL, documentTitleLocator.getAttribute(HREF_ATTRIBUTE))
                );
                docTitles.add(documentTitleLocator.innerText());
            }
            currentDocIndex = numberOfDocs - 1;
        } while (showMoreButton.getAttribute(DISABLED) == null);
    }

    private void scrapeDetail(Page page, Document scrapedDocument, String detail, Locator docInfoLocator) {

        String info = docInfoLocator.innerText();

        AuthorDocumentsPayload payload = new AuthorDocumentsPayload();

        switch (detail) {
            case DETAILS_AUTHORS -> payload.setCoAuthorsNames(extractAuthorDetails(page, info));
            case PUBLICATION_DATE -> payload.setPublicationDate(info);
            case DETAILS_JOURNAL, DETAILS_BOOK, DETAILS_CONFERENCE, DETAILS_SOURCE -> payload.setIssued(info);
            case DETAILS_VOLUME -> payload.setVolume(info);
            case DETAILS_ISSUE -> payload.setIssue(info);
            case DETAILS_PAGES -> payload.setPages(info);
            case DETAILS_PUBLISHERS -> payload.setPublisher(info);
            case DETAILS_DESCRIPTION -> payload.setDescription(info);
            case DETAILS_SCHOLAR_ARTICLES -> {
                String link = docInfoLocator.locator(ANCHOR_TAG).all().get(0).getAttribute(HREF_ATTRIBUTE);
                if (link.startsWith("/")) {
                    payload.setLink(String.format(GOOGLE_SCHOLAR_BASE_URL, link));
                    break;
                }
                payload.setLink(link);
            }
            case DETAILS_TOTAL_CITATIONS -> {
//                extractCitations(page, scrapedDocument, docInfoLocator.locator(ANCHOR_TAG).all().get(0));
            }
            default -> {
            }
        }
    }

    private List<String> extractAuthorDetails(Page page, String info) {

        String[] authors = info.split(",");

        List<String> coAuthors = new ArrayList<>();
        String currentUrl = page.url();

        for (int i = 0; i < authors.length; i++) {
            authors[i] = authors[i].trim();
            coAuthors.add(authors[i]);
        }

        page.navigate(currentUrl);

        return coAuthors;
    }

    private ScrapedEntity saveScrapedEntity(AuthorDocumentsPayload authorDocumentsPayload, UUID parentId, UUID sessionId) {

        ScrapedEntity scrapedEntity = new ScrapedEntity();
        scrapedEntity.setParentId(parentId);
        scrapedEntity.setSessionId(sessionId);
        scrapedEntity.setPayload(new Gson().toJson(authorDocumentsPayload));
        scrapedEntity.setType(ScrapedEntityType.DOCUMENT);
        scrapedEntity.setDataSource(DataSourceType.GOOGLE_SCHOLAR);

        return scrapedEntityRepository.save(scrapedEntity);
    }

}