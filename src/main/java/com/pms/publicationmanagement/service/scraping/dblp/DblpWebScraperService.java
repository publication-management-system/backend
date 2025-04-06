package com.pms.publicationmanagement.service.scraping.dblp;

import com.microsoft.playwright.*;
import com.pms.publicationmanagement.model.profiling.Author;
import com.pms.publicationmanagement.model.profiling.Citation;
import com.pms.publicationmanagement.model.profiling.Document;
import com.pms.publicationmanagement.repository.AuthorRepository;
import com.pms.publicationmanagement.repository.DocumentRepository;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DblpWebScraperService {

    public static final String DBLP_HOME_URL = "https://dblp.org/";
    public static final String DBLP_SEARCH_INPUT = "#completesearch-form";
    public static final String DBLP_AUTHOR_SEARCH_RESULT = "#completesearch-authors";
    public static final String EXACT_MATCH_RESULT_LIST = "[class = \"result-list\"]";
    public static final String AUTHOR_PUBL_SECTION = "#publ-section";
    public static final String PUBL_LIST_YEAR = "[class=\"year\"]";

    public static final String PUBL_LIST = "[class=\"publ-list\"]";

    private final DocumentRepository documentRepository;

    private final AuthorRepository authorRepository;
    public void scrape(String name){

        List<String> args = new ArrayList<>();
        args.add("-private");
        try (Playwright playwright = Playwright.create();
             Browser browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(false).setArgs(args))) {

            Page page = browser.newPage();
            getToAuthorProfile(name, page);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    private void getToAuthorProfile(String profileName, Page authorProfile) throws InterruptedException {
        authorProfile.navigate(DBLP_HOME_URL);
        authorProfile.locator(DBLP_SEARCH_INPUT).locator("input").fill(profileName);

        authorProfile.waitForSelector(DBLP_AUTHOR_SEARCH_RESULT);
        authorProfile.locator(EXACT_MATCH_RESULT_LIST).locator("li").all().get(0).locator("a").click();

        authorProfile.waitForSelector(AUTHOR_PUBL_SECTION);

        for(int periodIndex = 0; periodIndex < authorProfile.locator(AUTHOR_PUBL_SECTION).locator(PUBL_LIST).all().size(); periodIndex ++) {

            List<Locator> publicationLists = authorProfile.locator(AUTHOR_PUBL_SECTION).locator(PUBL_LIST).all().get(periodIndex).locator("li:has(cite)").all();

            for (Locator publicationLocator : publicationLists) {
                String xmlLink = publicationLocator.locator("nav").locator("ul").locator("li.drop-down")
                        .all().get(1).locator("div.body").locator("ul").first().locator("li")
                        .last().locator("a").getAttribute("href");


                Dblp docDetails = extractXmlObject(xmlLink);


                Document publication = new Document();

                if (docDetails.getArticle() != null) {

                        List<Author> listOfSavedAuthors = new ArrayList<>();
                        for(String authorName : docDetails.getArticle().author) {
                            Author author = new Author();
                            author.setName(authorName);
                            Author saved = authorRepository.save(author);
                            listOfSavedAuthors.add(saved);
                        }
                        publication.setAuthors(listOfSavedAuthors);
                        publication.setPublicationDate(docDetails.getArticle().year);
                        publication.setTitle(docDetails.getArticle().title);
                        publication.setPages(docDetails.getArticle().pages);
                        publication.setVolume(docDetails.getArticle().volume);
                        publication.setIssued(docDetails.getArticle().journal);
                        if(docDetails.getArticle().ee == null) {
                            publication.setLink("No link provided");
                        }
                        else {
                            publication.setLink(docDetails.getArticle().ee.get(0));
                        }

                }
                if (docDetails.getInproceedings() != null) {

                    List<Author> listOfSavedAuthors = new ArrayList<>();
                    for(String authorName : docDetails.getInproceedings().author) {
                        Author author = new Author();
                        author.setName(authorName);
                        Author saved = authorRepository.save(author);
                        listOfSavedAuthors.add(saved);
                    }

                    publication.setAuthors(listOfSavedAuthors);
                    publication.setPublicationDate(docDetails.getInproceedings().year);
                    publication.setTitle(docDetails.getInproceedings().title);
                    publication.setPages(docDetails.getInproceedings().pages);
                    publication.setIssued(docDetails.getInproceedings().booktitle);
                    if(docDetails.getInproceedings().ee == null) {
                        publication.setLink("No link provided");
                    }
                    else {
                        publication.setLink(docDetails.getInproceedings().ee.get(0));
                    }
                }


                if (docDetails.getProceedings() != null) {
                    List<Author> listOfSavedAuthors = new ArrayList<>();
                    for(String authorName : docDetails.getProceedings().editor) {
                        Author author = new Author();
                        author.setName(authorName);
                        Author saved = authorRepository.save(author);
                        listOfSavedAuthors.add(saved);
                    }
                    publication.setAuthors(listOfSavedAuthors);
                    publication.setPublicationDate(docDetails.getProceedings().year);
                    publication.setTitle(docDetails.getProceedings().title);
                    publication.setIssued(docDetails.getProceedings().booktitle);
                    publication.setPublisher(docDetails.getProceedings().publisher);
                    publication.setVolume(docDetails.getProceedings().volume);
                    //docDetails.getProceedings().series = numele seriei de carti ( nu exista in document)
                    if(docDetails.getProceedings().ee == null) {
                        publication.setLink("No link provided");
                    }
                    else {
                        publication.setLink(docDetails.getProceedings().ee);
                    }

                }

                String citationsPage = publicationLocator.locator("nav").locator("ul").locator(".drop-down").
                        first().locator("div.body").locator("ul").first().
                        locator("li.details").locator("a").getAttribute("href");

                authorProfile.navigate(citationsPage);

                authorProfile.waitForSelector("#references-load");

                if(!authorProfile.locator("#references-load").locator("input").isChecked()) {
                    authorProfile.locator("#references-load").locator("a").click();
                }

//                while(true) {
//                    if(authorProfile.locator("#publ-references-section").locator("div.hide-body")
//                            .locator("ul.publ-list").getAttribute("style").isBlank()) {
//                        break;
//                    }
//                }

                //poate trebuie thread.sleep?

                authorProfile.waitForSelector("#publ-references-section");

                Thread.sleep(1000);

                //trebuie un scrollIntoView

                List<Locator> citationsWithoutLink = authorProfile.locator("#publ-references-section").locator("li:has(img.no-match)").all();

                List<Locator> citations = authorProfile.locator("#publ-references-section").locator("li:has(nav.publ)").all();

                List<Citation> citationsToBeSaved = new ArrayList<>();

                for(Locator l : citations) {
                    Citation toBeAdded = new Citation();
                    toBeAdded.setTitle(l.locator("cite:has(span.title)").locator("span.title").innerText());
                    String citationXml = l.locator("nav").locator("ul").locator("li.drop-down")
                            .all().get(1).locator("div.body").locator("ul").first().locator("li")
                            .last().locator("a").getAttribute("href");
                    Dblp citationDetails = extractXmlObject(citationXml);
                    if(citationDetails.getProceedings() != null) {
                        toBeAdded.setLink(citationDetails.getProceedings().ee);
                    }
                    else if(citationDetails.getInproceedings() != null) {
                        toBeAdded.setLink(citationDetails.getInproceedings().ee.get(0));
                     }
                    else if(citationDetails.getArticle() != null) {
                        toBeAdded.setLink(citationDetails.getArticle().ee.get(0));
                    }

                    citationsToBeSaved.add(toBeAdded);
                }

                for(Locator l : citationsWithoutLink) {
                    Citation toBeAdded = new Citation();
                    toBeAdded.setTitle(l.locator("cite:has(span.title)").locator("span.title").innerText());
                    toBeAdded.setLink("No link found");
                    citationsToBeSaved.add(toBeAdded);
                }

                publication.setCitedIn(citationsToBeSaved);

                documentRepository.save(publication);

                authorProfile.goBack();

                
            }


        }

    }

    private static Dblp extractXmlObject(String xmlLink) {
        WebClient client = WebClient.create();
        String data = client.get().uri(xmlLink).retrieve().bodyToMono(String.class).block();
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


