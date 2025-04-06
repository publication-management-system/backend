package com.pms.publicationmanagement.service.scraping.googlescholar;

import com.google.gson.Gson;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.pms.publicationmanagement.model.scraping.DataSourceType;
import com.pms.publicationmanagement.model.scraping.ScrapedEntity;
import com.pms.publicationmanagement.model.scraping.ScrapedEntityType;
import com.pms.publicationmanagement.model.scraping.ScrapingSession;
import com.pms.publicationmanagement.model.scraping.payloads.AuthorProfilePayload;
import com.pms.publicationmanagement.repository.ScrapedEntityRepository;
import com.pms.publicationmanagement.service.scraping.IWebScrapingStep;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.pms.publicationmanagement.service.scraping.googlescholar.locators.GoogleScholarLocators.ANCHOR_TAG;
import static com.pms.publicationmanagement.service.scraping.googlescholar.locators.GoogleScholarLocators.HREF_ATTRIBUTE;
import static com.pms.publicationmanagement.service.scraping.googlescholar.locators.GoogleScholarProfilePageLocators.CITATIONS_CONTAINER_ID;
import static com.pms.publicationmanagement.service.scraping.googlescholar.locators.GoogleScholarProfilePageLocators.PROFILE_PAGE_DETAILS;
import static com.pms.publicationmanagement.service.scraping.googlescholar.locators.GoogleScholarProfilePageLocators.PROFILE_PAGE_DOCUMENTS_BY_YEAR;
import static com.pms.publicationmanagement.service.scraping.googlescholar.locators.GoogleScholarProfilePageLocators.PROFILE_PAGE_INSTITUTION;
import static com.pms.publicationmanagement.service.scraping.googlescholar.locators.GoogleScholarProfilePageLocators.PROFILE_PAGE_NAME;
import static com.pms.publicationmanagement.service.scraping.googlescholar.locators.GoogleScholarProfilePageLocators.PROFILE_PAGE_ROLE;
import static com.pms.publicationmanagement.service.scraping.googlescholar.locators.GoogleScholarProfilePageLocators.PROFILE_PHOTO_ICON;
import static com.pms.publicationmanagement.service.scraping.googlescholar.locators.GoogleScholarProfilePageLocators.PROFILE_SEARCH_RESULT;
import static com.pms.publicationmanagement.service.scraping.googlescholar.locators.GoogleScholarProfilePageLocators.SCHOLAR_START_INPUT;
import static com.pms.publicationmanagement.service.scraping.googlescholar.locators.GoogleScholarProfilePageLocators.SCHOLAR_START_RESULT;
import static com.pms.publicationmanagement.service.scraping.googlescholar.locators.GoogleScholarProfilePageLocators.SCHOLAR_START_SEARCH;
import static com.pms.publicationmanagement.service.scraping.googlescholar.locators.GoogleScholarProfilePageLocators.START_URL;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleScholarProfileScraping implements IWebScrapingStep {
    private final ScrapedEntityRepository scrapedEntityRepository;

    @Override
    public void scrapeEntity(Page page, ScrapingSession scrapingSession, UUID parentId) {
        log.info("GOOGLE_SCHOLAR: Searching for {} {}", scrapingSession.getFirstName(), scrapingSession.getLastName());
        String name = String.format("%s %s", scrapingSession.getFirstName(), scrapingSession.getLastName());

        page.navigate(START_URL);
        page.locator(SCHOLAR_START_INPUT).fill(name);
        page.locator(SCHOLAR_START_SEARCH).click();
        page.waitForSelector(CITATIONS_CONTAINER_ID);
        List<Locator> allProfiles = page.locator(CITATIONS_CONTAINER_ID).locator(SCHOLAR_START_RESULT).all();
        if (allProfiles.isEmpty()) {
            log.warn("Found no profiles for {}, in GOOGLE_SCHOLAR", name);
        }

        Locator profileLinksSection = allProfiles.get(0);
        profileLinksSection.locator(ANCHOR_TAG).all().get(0).click();

        page.waitForSelector(PROFILE_SEARCH_RESULT);
        page.locator(PROFILE_SEARCH_RESULT).locator(PROFILE_PHOTO_ICON).all().get(0).click();
        page.locator(PROFILE_PAGE_DOCUMENTS_BY_YEAR).click(); //sort by year


        Locator detailsBox = page.locator(PROFILE_PAGE_DETAILS);

        String authorName = detailsBox.locator(PROFILE_PAGE_NAME).textContent();
        String[] roleAndInstitution = detailsBox.locator(PROFILE_PAGE_ROLE).all().get(0).textContent().split(",");
        String institutionPage;
        if (!page.locator(PROFILE_PAGE_INSTITUTION).locator(ANCHOR_TAG).all().isEmpty()) {
            institutionPage = page.locator(PROFILE_PAGE_INSTITUTION).locator(ANCHOR_TAG).all().get(0).getAttribute(HREF_ATTRIBUTE);
        } else {
            institutionPage = page.locator(PROFILE_PAGE_INSTITUTION).innerText();
        }

        AuthorProfilePayload payload = new AuthorProfilePayload();
        payload.setAuthorName(authorName);

        if (roleAndInstitution.length > 1) {
            payload.setInstitutionRole(roleAndInstitution[0]);
            payload.setInstitution(roleAndInstitution[1]);
        } else {
            payload.setInstitution(roleAndInstitution[0]);
        }

        payload.setEmail(institutionPage);

        saveScrapedEntity(payload, parentId, scrapingSession.getId());
    }

    private void saveScrapedEntity(AuthorProfilePayload authorProfilePayload, UUID parentId, UUID sessionId) {
        ScrapedEntity scrapedEntity = new ScrapedEntity();
        scrapedEntity.setParentId(parentId);
        scrapedEntity.setSessionId(sessionId);
        scrapedEntity.setPayload(new Gson().toJson(authorProfilePayload));
        scrapedEntity.setType(ScrapedEntityType.AUTHOR);
        scrapedEntity.setDataSource(DataSourceType.GOOGLE_SCHOLAR);

        scrapedEntityRepository.save(scrapedEntity);
    }
}
