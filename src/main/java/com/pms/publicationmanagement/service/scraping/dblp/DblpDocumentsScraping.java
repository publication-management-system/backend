package com.pms.publicationmanagement.service.scraping.dblp;

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
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.StringReader;
import java.util.List;
import java.util.Objects;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class DblpDocumentsScraping implements IWebScrapingStep {
    public static final String DBLP_HOME_URL = "https://dblp.org/";
    public static final String DBLP_SEARCH_INPUT = "#completesearch-form";
    public static final String DBLP_AUTHOR_SEARCH_RESULT = "#completesearch-authors";
    public static final String EXACT_MATCH_RESULT_LIST = "[class = \"result-list\"]";
    public static final String AUTHOR_PUBL_SECTION = "#publ-section";
    public static final String PUBL_LIST = "[class=\"publ-list\"]";

    private final ScrapedEntityRepository scrapedEntityRepository;

    private final WebClient webClient;

    @Override
    public void scrapeEntity(Page page, ScrapingSession scrapingSession, UUID parentId) {
        String nameToBeSearched = scrapingSession.getFirstName() + " " + scrapingSession.getLastName();

        page.navigate(DBLP_HOME_URL);
        page.locator(DBLP_SEARCH_INPUT).locator("input").fill(nameToBeSearched);

        page.waitForSelector(DBLP_AUTHOR_SEARCH_RESULT);
        page.locator(EXACT_MATCH_RESULT_LIST).locator("li").all().get(0).locator("a").click();

        page.waitForSelector(AUTHOR_PUBL_SECTION);

        AuthorDocumentsPayload payload = new AuthorDocumentsPayload();

        for (int periodIndex = 0; periodIndex < page.locator(AUTHOR_PUBL_SECTION).locator(PUBL_LIST).all().size(); periodIndex++) {

            List<Locator> publicationLists = page.locator(AUTHOR_PUBL_SECTION).locator(PUBL_LIST).all().get(periodIndex).locator("li:has(cite)").all();

            for (Locator publicationLocator : publicationLists) {
                String xmlLink = publicationLocator.locator("nav").locator("ul").locator("li.drop-down")
                        .all().get(1).locator("div.body").locator("ul").first().locator("li")
                        .last().locator("a").getAttribute("href");


                Dblp docDetails = extractXmlObject(xmlLink);

                log.info("Scraping document {}", publicationLocator.locator("span.title").innerText());

                Document publication = new Document();

                if (docDetails.getArticle() != null) {
                    payload.setCoAuthorsNames(docDetails.getArticle().author);
                    payload.setPublicationDate(docDetails.getArticle().year);
                    payload.setTitle(docDetails.getArticle().title);
                    payload.setPages(docDetails.getArticle().pages);
                    payload.setVolume(docDetails.getArticle().volume);
                    payload.setIssued(docDetails.getArticle().journal);
                    if (docDetails.getArticle().ee == null) {
                        publication.setLink("No link provided");
                    } else {
                        publication.setLink(docDetails.getArticle().ee.get(0));
                    }
                }
                else if (docDetails.getInproceedings() != null) {
                    payload.setCoAuthorsNames(docDetails.getInproceedings().author);
                    payload.setPublicationDate(docDetails.getInproceedings().year);
                    payload.setTitle(docDetails.getInproceedings().title);
                    payload.setPages(docDetails.getInproceedings().pages);
                    payload.setIssued(docDetails.getInproceedings().booktitle);
                    publication.setLink(Objects.requireNonNull(docDetails.getInproceedings().ee.get(0), "No link provided"));
                }
                else if (docDetails.getProceedings() != null) {
                    payload.setCoAuthorsNames(docDetails.getProceedings().editor);
                    payload.setPublicationDate(docDetails.getProceedings().year);
                    payload.setTitle(docDetails.getProceedings().title);
                    payload.setIssued(docDetails.getProceedings().booktitle);
                    payload.setPublisher(docDetails.getProceedings().publisher);
                    payload.setVolume(docDetails.getProceedings().volume);
                    publication.setLink(Objects.requireNonNullElse(docDetails.getProceedings().ee, "No link provided"));
                }
            }
        }

        saveScrapedEntity(payload, parentId, scrapingSession.getId());
    }

    private void saveScrapedEntity(AuthorDocumentsPayload payload, UUID parentId, UUID sessionId) {
        ScrapedEntity scrapedEntity = new ScrapedEntity();
        scrapedEntity.setParentId(parentId);
        scrapedEntity.setSessionId(sessionId);
        scrapedEntity.setPayload(new Gson().toJson(payload));
        scrapedEntity.setType(ScrapedEntityType.DOCUMENT);
        scrapedEntity.setDataSource(DataSourceType.DBLP);

        scrapedEntityRepository.save(scrapedEntity);
    }

    public Dblp extractXmlObject(String xmlLink) { //public pt ca e folosita in citation
        String data = webClient.get().uri(xmlLink).retrieve().bodyToMono(String.class).block();
        Dblp docDetails = null;
        try {
            Unmarshaller decoder = JAXBContext.newInstance(Dblp.class).createUnmarshaller();
            assert data != null;
            docDetails = (Dblp) decoder.unmarshal(new StringReader(data));

        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
        return docDetails;
    }
}

