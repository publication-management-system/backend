package com.pms.publicationmanagement.services;

import com.microsoft.playwright.*;
import com.pms.publicationmanagement.model.Author;
import com.pms.publicationmanagement.repository.SpringJpaAuthorRepository;
import org.springframework.stereotype.Service;

@Service
public class GoogleScholarWebScraperService {

    private final SpringJpaAuthorRepository springJpaAuthorRepository;

    public GoogleScholarWebScraperService(SpringJpaAuthorRepository springJpaAuthorRepository) {
        this.springJpaAuthorRepository = springJpaAuthorRepository;
    }


    public void scrape(String name) {
        try (Playwright playwright = Playwright.create();
             Browser browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(false));) {

            Page page = browser.newPage();
            page.navigate("https://scholar.google.com");
            page.locator("#gs_hdr_tsi").fill(name);
            page.locator("#gs_hdr_tsb").click();

            page.waitForSelector("#gs_res_ccl_mid");
            Locator profileLinksSection = page.locator("#gs_res_ccl_mid").locator("[class=gs_r]").all().get(0);
            profileLinksSection.locator("a").all().get(0).click();

            page.waitForSelector("#gsc_sa_ccl");
            page.locator("#gsc_sa_ccl").locator("[class=\"gs_ai_pho\"]").all().get(0).click();
            page.locator("#gsc_a_ha").click();


            Locator detailsBox = page.locator("#gsc_prf");

            String authorName = detailsBox.locator("#gsc_prf_in").textContent();
            String[] roleAndInstitution = detailsBox.locator("[class=\"gsc_prf_il\"]").all().get(0).textContent().split(",");
            String institutionPage = page.locator("#gsc_prf_ivh").locator("a").all().get(0).getAttribute("href");

            Author scrapedAuthor = new Author();
            scrapedAuthor.setName(authorName);

            if (roleAndInstitution.length > 1) {
                scrapedAuthor.setRole(roleAndInstitution[0]);
                scrapedAuthor.setInstitution(roleAndInstitution[1]);
            } else {
                scrapedAuthor.setInstitution(roleAndInstitution[0]);
            }

            scrapedAuthor.setInstitutionMail(institutionPage);

            springJpaAuthorRepository.save(scrapedAuthor);


            System.out.println(page.title());

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

}