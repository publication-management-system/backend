package com.pms.publicationmanagement.services.scraping.scopus;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import com.pms.publicationmanagement.model.Author;
import com.pms.publicationmanagement.model.Document;
import com.pms.publicationmanagement.repository.SpringJpaAuthorRepository;
import com.pms.publicationmanagement.repository.SpringJpaDocumentRepository;
import org.springframework.stereotype.Service;

import javax.print.Doc;
import java.util.ArrayList;
import java.util.List;

@Service
public class ScopusWebScraperService {

    public static final String SCOPUS_HOME_PAGE = "https://www.scopus.com/home.uri?zone=header&origin=AuthorProfile";
    public static final String AUTHOR_SEARCH_ICON = "a.Button-module__nwgBo:has(span.GlobalHeader-module__QnYPZ)";
    public static final String AUTHOR_SEARCH_FIRST_RESULT = "#resultDataRow1";
    public static final String DOCUMENTS_CONTAINER_ID = "#documents-panel";
    public static final String PUBLICATION_YEAR_SPAN = "span.Typography-module__lVnit:has(a)";
    private final SpringJpaAuthorRepository springJpaAuthorRepository;

    private final SpringJpaDocumentRepository springJpaDocumentRepository;

    public ScopusWebScraperService(SpringJpaAuthorRepository springJpaAuthorRepository, SpringJpaDocumentRepository springJpaDocumentRepository) {
        this.springJpaAuthorRepository = springJpaAuthorRepository;
        this.springJpaDocumentRepository = springJpaDocumentRepository;
    }

    public void scrape(String lastName, String firstName) {
        List<String> args = new ArrayList<>();
        args.add("-private");
        try (Playwright playwright = Playwright.create();
             Browser browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(false).setArgs(args))) {

            Page page = browser.newPage();
            getToAuthorProfile(lastName, firstName, page);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void getToAuthorProfile(String lastName, String firstName, Page page) throws InterruptedException {

        page.navigate(SCOPUS_HOME_PAGE);
        page.locator(AUTHOR_SEARCH_ICON).click();
        page.getByLabel("Author last name").fill(lastName);
        page.getByLabel("Author first name").fill(firstName);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Search")).first().click();
        page.locator(AUTHOR_SEARCH_FIRST_RESULT).locator("td").locator("a").click();
        if(springJpaAuthorRepository.findByName(lastName + " " + firstName).isEmpty()) {
            Author newAuthor = new Author();
            newAuthor.setName(lastName + firstName);
            newAuthor.addDocuments(scrapeDocuments(page));
            springJpaAuthorRepository.save(newAuthor);
        }
        else {
            Author found = springJpaAuthorRepository.findByName(lastName + " " + firstName).get(0);
            found.addDocuments(scrapeDocuments(page));
            //mai trebuie dat save?
        }

    }

    private List<Document> scrapeDocuments(Page page) throws InterruptedException {

        page.locator(DOCUMENTS_CONTAINER_ID).scrollIntoViewIfNeeded();
        Thread.sleep(1000);

        List<Document> authorDocuments = new ArrayList<>();

        List<Locator> docLocators = page.getByTestId("results-list-item").all();
        for(Locator l : docLocators) {
            Document publication = new Document();

            publication.setTitle(l.locator("h4").innerText());

            List<String> authorNames = l.getByTestId("author-list").locator("span").
                    locator("a").locator("span").allInnerTexts();

            List<Author> listOfSavedAuthors = new ArrayList<>();

            for(String name : authorNames) { //sunt prescurtate xD
                Author author = new Author();
                author.setName(name);
                Author saved = springJpaAuthorRepository.save(author);
                listOfSavedAuthors.add(saved);
            }
            publication.setAuthors(listOfSavedAuthors);

            publication.setIssued(l.locator("em").last().innerText());

            publication.setPublicationDate(l.locator(PUBLICATION_YEAR_SPAN).innerText().split(",")[1]);

            Document saved = springJpaDocumentRepository.save(publication);
            authorDocuments.add(saved);
        }
        return authorDocuments;
    }
}
