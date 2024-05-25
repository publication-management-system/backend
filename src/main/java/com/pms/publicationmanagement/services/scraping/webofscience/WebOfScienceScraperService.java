package com.pms.publicationmanagement.services.scraping.webofscience;

import com.microsoft.playwright.*;
import com.pms.publicationmanagement.model.Author;
import com.pms.publicationmanagement.model.Citation;
import com.pms.publicationmanagement.model.Document;
import com.pms.publicationmanagement.repository.SpringJpaAuthorRepository;
import com.pms.publicationmanagement.repository.SpringJpaCitationRepository;
import com.pms.publicationmanagement.repository.SpringJpaDocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WebOfScienceScraperService {

    private final SpringJpaAuthorRepository springJpaAuthorRepository;

    private final SpringJpaDocumentRepository springJpaDocumentRepository;

    private  final SpringJpaCitationRepository springJpaCitationRepository;
    public void scrape(String lastName, String firstName) {
        List<String> args = new ArrayList<>();
        args.add("-private");
        try (Playwright playwright = Playwright.create();
             Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false).setArgs(args))) {

            Page page = browser.newPage();
            authenticate(page);
            getToAuthorProfile(lastName, firstName, page);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void authenticate(Page page) {
        page.navigate("https://access.clarivate.com/login?app=wos&alternative=true&shibShireURL=https:%2F%2Fwww.webofknowledge.com%2F%3Fauth%3DShibboleth&shibReturnURL=https:%2F%2Fwww.webofknowledge.com%2F&roaming=true");
        page.locator("#signIn-btn").click();
    }

    private void getToAuthorProfile(String lastName, String firstName, Page page) throws InterruptedException {
        page.waitForSelector("app-author-search");
        page.locator("#onetrust-reject-all-handler").click();
        page.locator("#mat-input-0").fill(lastName);
        page.locator("#mat-input-1").fill(firstName);
        page.locator("div.form-actions").locator("button").last().click();
        page.waitForSelector("div.results-column");
        List<String> profileNames = page.locator("div.results-column").locator("app-author-search-result-card").locator("h3.wat-card-title").locator("span.ng-star-inserted").allInnerTexts();
        List<Locator> profileLinks = page.locator("div.results-column").locator("app-author-search-result-card").locator("h3.wat-card-title").locator("a").all();
        boolean firstNameFound = false;
        boolean lastNameFound = false;
        int suntProst = 0;
        for(String author : profileNames) {
            String[] authorNames =  author.split("\\W");
            for (String authorName : authorNames) {
                if (authorName.equalsIgnoreCase(lastName)) {
                    lastNameFound = true;
                }
                if (authorName.equalsIgnoreCase(firstName)) {
                    firstNameFound = true;
                }
            }
            if(lastNameFound && firstNameFound) {
                break;
            }
            lastNameFound = false;
            firstNameFound = false;
            suntProst++;
        }
        profileLinks.get(suntProst).click();
        getPageOfPublications(page);
        Locator nextPageButton = page.locator("form.pagination").locator("button").last();

        while (nextPageButton.isEnabled()) {
            nextPageButton.click();
            getPageOfPublications(page);
            nextPageButton = nextPageButton = page.locator("form.pagination").locator("button").last();;
        }



    }

    private void getPageOfPublications(Page page) throws InterruptedException {
        page.waitForSelector("app-publications-tab"); //trebuie un wait mai bun
        Thread.sleep(1000);
        List<Locator> publicationsLocators = page.locator("app-publications-tab").locator("app-publication-card").all();
        for(Locator l : publicationsLocators) {
            l.locator("div.left-section").locator("a").first().click();
            page.waitForSelector("#snMainArticle");
            String[] authors = page.locator("#SumAuthTa-MainDiv-author-en").locator("span.cdx-grid-data").innerText().split(";");
            List<Author> listOfSavedAuthors = new ArrayList<>();
            for(String authorName : authors) {
                Author author = new Author();
                authorName = authorName.replace(",", "");
                authorName = authorName.substring(authorName.indexOf("(") + 1, authorName.indexOf(")"));
                author.setName(authorName);
                Author saved = springJpaAuthorRepository.save(author);
                listOfSavedAuthors.add(saved);
            }
            Document publication = new Document();
            publication.setAuthors(listOfSavedAuthors);

            publication.setTitle(page.locator("#FullRTa-fullRecordtitle-0").innerText());

            String source = page.locator("mat-sidenav-content").first().innerText();
            publication.setIssued(source);

            String publicationDate = page.locator("#FullRTa-pubdate").innerText();
            publication.setPublicationDate(publicationDate);

            //poate adaug article number?
            if(!page.locator("#FullRTa-volume").all().isEmpty()) {
                publication.setVolume(page.locator("#FullRTa-volume").innerText());
            }

            if(!page.locator("#FullRTa-abstract-basic").all().isEmpty()) {
                publication.setDescription(page.locator("#FullRTa-abstract-basic").innerText());
            }

            if(!page.locator("#FullRTa-issue").all().isEmpty()) {
                publication.setIssue(page.locator("#FullRTa-issue").innerText());
            }

            if(!page.locator("#FullRTa-pageNo").all().isEmpty()) {
                publication.setPages(page.locator("#FullRTa-pageNo").innerText());
            }

//            https://dx.doi.org/
//            String link = page.locator("#FullRTa-DOI").innerText(); //se poate gasi link din asta(nu stiu daca trebuie)

            page.goBack();

            page.waitForSelector("app-publications-tab"); //trebuie un wait mai bun

            Locator rightSideCitationLinkNumber = l.locator("div.right-section").locator("div").last().locator("a");
            if(!rightSideCitationLinkNumber.all().isEmpty()) {
                rightSideCitationLinkNumber.click();
                page.waitForSelector("#GenericFD-search-searchInfo-parent");
                List<Locator> citationLocatorsList = page.locator("app-records-list").locator("app-record").locator("div.data-section").all();

                List<Citation> citationsToBeAdded = new ArrayList<>();

                for(Locator citationLocator : citationLocatorsList) {
                    citationLocator.scrollIntoViewIfNeeded();
                    Citation citation = new Citation();
                    citation.setTitle(citationLocator.locator("app-summary-title").innerText());
                    citationLocator.locator("app-summary-title").locator("a").click();

                    if (page.isVisible("#FullRTa-DOI")) {
                        String link = page.locator("#FullRTa-DOI").innerText();
                        citation.setLink(link);
                    }

                    citationsToBeAdded.add(citation);
                    page.goBack();
                }

                publication.setCitedIn(citationsToBeAdded);
                page.goBack();
                page.waitForSelector("app-publications-placeholder");
            }

            springJpaDocumentRepository.save(publication);


        }
    }

}

