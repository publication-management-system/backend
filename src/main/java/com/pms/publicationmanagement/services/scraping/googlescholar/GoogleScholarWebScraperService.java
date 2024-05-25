package com.pms.publicationmanagement.services.scraping.googlescholar;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.pms.publicationmanagement.model.Author;
import com.pms.publicationmanagement.model.Citation;
import com.pms.publicationmanagement.model.Document;
import com.pms.publicationmanagement.repository.SpringJpaAuthorRepository;
import com.pms.publicationmanagement.repository.SpringJpaDocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.pms.publicationmanagement.services.scraping.googlescholar.GoogleScholarLocators.*;

@Service
@RequiredArgsConstructor
public class GoogleScholarWebScraperService {
    public static final String CITATIONS_CONTAINER_ID = "#gs_res_ccl_mid"; //se cheama la fel cu rezultatul de la search initial(pt dorel lucanu)
    public static final String CITATION_ROW = "[class=\"gs_r gs_or gs_scl\"]";
    public static final String HEADER_3 = "h3";
    public static final String SPAN = "span";
    public static final String CITATION_UNAVAILABLE_AUTHOR = "[class=\"gs_a\"]";
    public static final String START_URL = "https://scholar.google.com/?hl=en&as_sdt=0,5";
    public static final String SCHOLAR_START_INPUT = "#gs_hdr_tsi";
    public static final String SCHOLAR_START_SEARCH = "#gs_hdr_tsb";
    public static final String SCHOLAR_START_RESULT = "[class=\"gs_r\"]";
    public static final String PROFILE_SEARCH_RESULT = "#gsc_sa_ccl";
    public static final String PROFILE_PHOTO_ICON = "[class=\"gs_ai_pho\"]";
    public static final String PROFILE_PAGE_DOCUMENTS_BY_YEAR = "#gsc_a_ha";
    public static final String PROFILE_PAGE_DETAILS = "#gsc_prf";
    public static final String PROFILE_PAGE_NAME = "#gsc_prf_in";
    public static final String PROFILE_PAGE_ROLE = "[class=\"gsc_prf_il\"]";
    public static final String PROFILE_PAGE_INSTITUTION = "#gsc_prf_ivh";
    private final SpringJpaAuthorRepository springJpaAuthorRepository;

    private final SpringJpaDocumentRepository springJpaDocumentRepository;
    public void scrape(String name) {
        List<String> args = new ArrayList<>();
        args.add("-private");
        try (Playwright playwright = Playwright.create();
             Browser browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(false).setArgs(args))) {
            Page page = browser.newPage();

            getScholarProfile(name, page);

            Locator showMoreButton = page.locator(SHOW_MORE_BUTTON_ID);
            List<String> docLinks = new ArrayList<>();
            List<String> docTitles = new ArrayList<>();

            clickOnShowMoreUntilNoMore(page, showMoreButton, docLinks, docTitles);
            scrapeDocuments(page, docLinks, docTitles);

            System.out.println(page.title());

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private static void clickOnShowMoreUntilNoMore(Page page,
                                                   Locator showMoreButton,
                                                   List<String> docLinks,
                                                   List<String> docTitles) {
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

    private void scrapeDocuments(Page page, List<String> docLinks, List<String> docTitles) {
        for (int currentDoc = 0; currentDoc < docLinks.size(); currentDoc++) {
            if (!springJpaDocumentRepository.findByTitle(docTitles.get(currentDoc)).isEmpty()) {
                System.out.println("Did not find doc by title"); // nu inseamna ca am gasit documentul? not .isEmpty
                continue;
            }

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

            springJpaDocumentRepository.save(scrapedDocument);
        }
    }

    private void scrapeDetail(Page page, Document scrapedDocument, String detail, Locator docInfoLocator) {
        String info = docInfoLocator.innerText();

        switch (detail) {
            case DETAILS_AUTHORS -> scrapedDocument.setAuthors(extractAuthorDetails(page, info));
            case PUBLICATION_DATE -> scrapedDocument.setPublicationDate(info);
            case DETAILS_JOURNAL, DETAILS_BOOK, DETAILS_CONFERENCE, DETAILS_SOURCE -> scrapedDocument.setIssued(info);
            case DETAILS_VOLUME -> scrapedDocument.setVolume(info);
            case DETAILS_ISSUE -> scrapedDocument.setIssue(info);
            case DETAILS_PAGES -> scrapedDocument.setPages(info);
            case DETAILS_PUBLISHERS -> scrapedDocument.setPublisher(info);
            case DETAILS_DESCRIPTION -> scrapedDocument.setDescription(info);
            case DETAILS_SCHOLAR_ARTICLES -> {
                String link = docInfoLocator.locator(ANCHOR_TAG).all().get(0).getAttribute(HREF_ATTRIBUTE);
                if (link.startsWith("/")) {
                    scrapedDocument.setLink(String.format(GOOGLE_SCHOLAR_BASE_URL, link));
                    break;
                }
                scrapedDocument.setLink(link);
            }
            case DETAILS_TOTAL_CITATIONS -> {
                extractCitations(page, scrapedDocument, docInfoLocator.locator(ANCHOR_TAG).all().get(0));
            }
            default -> {
            }
        }
    }

    private void extractCitations(Page page, Document scrapedDocument, Locator firstLink) {
        String docDetailUrl = page.url();
        firstLink.click();

        List<Citation> citationList = new ArrayList<>();
        addPageOfCitations(citationList, page);

        List<Locator> pageColumns = page.locator(CITATIONS_PAGINATION).locator(TABLE_ROW).locator(TABLE_COLUMN)
                .all();
        Locator nextPage = pageColumns.size() > 0 ? pageColumns.get(pageColumns.size() - 1) : null;

        while (nextPage != null && nextPage.locator("b").isVisible()) {
            nextPage.click();
            addPageOfCitations(citationList, page);
        }

        scrapedDocument.setCitedIn(citationList);
        page.navigate(docDetailUrl);
    }

    private void addPageOfCitations(List<Citation> citationList, Page citationPage) {
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
            Citation citationToBeAdded = new Citation();
            citationToBeAdded.setLink(citationLink);
            citationToBeAdded.setTitle(citationTitle);
            citationList.add(citationToBeAdded);
        }
    }

    private List<Author> extractAuthorDetails(Page page, String info) {
        String[] authors = info.split(",");

        List<Author> coAuthors = new ArrayList<>();
        String currentUrl = page.url();

        for (int i = 0; i < authors.length; i++) {
            authors[i] = authors[i].trim();

            if (springJpaAuthorRepository.findByName(authors[i]).isEmpty()) {
                coAuthors.add(getScholarProfile(authors[i], page));
            } else {
                coAuthors.add(springJpaAuthorRepository.findByName(authors[i]).get(0));
            }
        }

        page.navigate(currentUrl);

        return coAuthors;
    }

    private Author getScholarProfile(String name, Page authorProfile) {
        authorProfile.navigate(START_URL);
        authorProfile.locator(SCHOLAR_START_INPUT).fill(name);
        authorProfile.locator(SCHOLAR_START_SEARCH).click();

        authorProfile.waitForSelector(CITATIONS_CONTAINER_ID);

        List<Locator> profileChecker = authorProfile.locator(CITATIONS_CONTAINER_ID).locator(SCHOLAR_START_RESULT).all();
        Author scrapedAuthor = new Author();

        if (profileChecker.isEmpty()) {
            scrapedAuthor.setName(name);
            springJpaAuthorRepository.save(scrapedAuthor);
            return scrapedAuthor;
        }

        Locator profileLinksSection = profileChecker.get(0);
        profileLinksSection.locator(ANCHOR_TAG).all().get(0).click();

        authorProfile.waitForSelector(PROFILE_SEARCH_RESULT);
        authorProfile.locator(PROFILE_SEARCH_RESULT).locator(PROFILE_PHOTO_ICON).all().get(0).click();
        authorProfile.locator(PROFILE_PAGE_DOCUMENTS_BY_YEAR).click(); //sort by year


        Locator detailsBox = authorProfile.locator(PROFILE_PAGE_DETAILS);

        String authorName = detailsBox.locator(PROFILE_PAGE_NAME).textContent();
        String[] roleAndInstitution = detailsBox.locator(PROFILE_PAGE_ROLE).all().get(0).textContent().split(",");
        String institutionPage;
        if (!authorProfile.locator(PROFILE_PAGE_INSTITUTION).locator(ANCHOR_TAG).all().isEmpty()) {
            institutionPage = authorProfile.locator(PROFILE_PAGE_INSTITUTION).locator(ANCHOR_TAG).all().get(0).getAttribute(HREF_ATTRIBUTE);
        } else {
            institutionPage = authorProfile.locator(PROFILE_PAGE_INSTITUTION).innerText();
        }

        scrapedAuthor.setName(authorName);

        if (roleAndInstitution.length > 1) {
            scrapedAuthor.setRole(roleAndInstitution[0]);
            scrapedAuthor.setInstitution(roleAndInstitution[1]);
        } else {
            scrapedAuthor.setInstitution(roleAndInstitution[0]);
        }

        scrapedAuthor.setInstitutionMail(institutionPage);

        if (springJpaAuthorRepository.findByName(name).isEmpty()) {
            springJpaAuthorRepository.save(scrapedAuthor);
        }

        return scrapedAuthor;
    }
}
