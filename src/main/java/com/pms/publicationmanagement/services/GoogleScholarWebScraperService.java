package com.pms.publicationmanagement.services;

import com.microsoft.playwright.*;
import com.pms.publicationmanagement.model.Author;
import com.pms.publicationmanagement.model.Document;
import com.pms.publicationmanagement.repository.SpringJpaAuthorRepository;
import com.pms.publicationmanagement.repository.SpringJpaDocumentRepository;
import org.springframework.stereotype.Service;

import javax.print.Doc;

import java.util.ArrayList;
import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@Service
public class GoogleScholarWebScraperService {

    private final SpringJpaAuthorRepository springJpaAuthorRepository;

    private final SpringJpaDocumentRepository springJpaDocumentRepository;

    public GoogleScholarWebScraperService(SpringJpaAuthorRepository springJpaAuthorRepository, SpringJpaDocumentRepository springJpaDocumentRepository) {
        this.springJpaAuthorRepository = springJpaAuthorRepository;
        this.springJpaDocumentRepository = springJpaDocumentRepository;
    }

    public Author getScholarProfile(String name, Page authorProfile) {

        Author scrapedAuthor = new Author();

        authorProfile.navigate("https://scholar.google.com/?hl=en&as_sdt=0,5");
        authorProfile.locator("#gs_hdr_tsi").fill(name);
        authorProfile.locator("#gs_hdr_tsb").click();

        authorProfile.waitForSelector("#gs_res_ccl_mid");

        List<Locator> profileChecker = authorProfile.locator("#gs_res_ccl_mid").locator("[class=gs_r]").all();
        if(profileChecker.isEmpty()) {
            scrapedAuthor.setName(name);
            springJpaAuthorRepository.save(scrapedAuthor);
            return scrapedAuthor;
        }
        Locator profileLinksSection = authorProfile.locator("#gs_res_ccl_mid").locator("[class=gs_r]").all().get(0);
        profileLinksSection.locator("a").all().get(0).click();

        authorProfile.waitForSelector("#gsc_sa_ccl");
        authorProfile.locator("#gsc_sa_ccl").locator("[class=\"gs_ai_pho\"]").all().get(0).click();
        authorProfile.locator("#gsc_a_ha").click(); //sort by year


        Locator detailsBox = authorProfile.locator("#gsc_prf");

        String authorName = detailsBox.locator("#gsc_prf_in").textContent();
        String[] roleAndInstitution = detailsBox.locator("[class=\"gsc_prf_il\"]").all().get(0).textContent().split(",");
        String institutionPage = authorProfile.locator("#gsc_prf_ivh").locator("a").all().get(0).getAttribute("href");

        scrapedAuthor.setName(authorName);

        if (roleAndInstitution.length > 1) {
            scrapedAuthor.setRole(roleAndInstitution[0]);
            scrapedAuthor.setInstitution(roleAndInstitution[1]);
        } else {
            scrapedAuthor.setInstitution(roleAndInstitution[0]);
        }

        scrapedAuthor.setInstitutionMail(institutionPage);

        springJpaAuthorRepository.save(scrapedAuthor);
        return scrapedAuthor;
    }


    public void scrape(String name) {
        try (Playwright playwright = Playwright.create();
             Browser browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(false))) {
            Page page = browser.newPage();
            getScholarProfile(name, page);

            //press show more

            Locator showMoreBox = page.locator("#gsc_bpf_more");

            while(showMoreBox.getAttribute("disabled")==null ) {
                showMoreBox.click();
            }

            int docNumber = 0;
            Document scrapedDocument = new Document();
//
            Locator docTable = page.locator("#gsc_a_b");

            String docName = docTable.locator("tr").all().get(docNumber).locator("[class=\"gsc_a_t\"]").all().get(0).locator("a").innerText();

            if(springJpaDocumentRepository.findByTitle(docName).isEmpty()) {

                docTable.locator("tr").all().get(docNumber).
                        locator("[class=\"gsc_a_t\"]").all().get(0).click();
                page.waitForSelector("#gsc_oci_table");
                List<Locator> docDetails = page.locator("#gsc_oci_table").locator("[class=\"gs_scl\"]")
                        .locator("[class=\"gsc_oci_field\"]").all();


                List<Locator> docInfo =  page.locator("#gsc_oci_table").locator("[class=\"gs_scl\"]").locator("[class=\"gsc_oci_value\"]").all();

                int i =0;
                String detail, info;

                while(i < docDetails.size()) {
                    detail = docDetails.get(i).innerText();
                    info = docInfo.get(i).innerText();

                    //e limba romana pe browser

                    switch (detail) {
                        case "Autori":
                        String[] authors = info.split(",");
                        List<Author> coAuthors = new ArrayList<>();
                        String currentUrl = page.url();
                            for (int j=0; j<authors.length; j++) {
                                authors[j] = authors[j].trim();
                                if (springJpaAuthorRepository.findByName(authors[j]).isEmpty()) { //cazuri cand numele are diacritice(in bd nu se salveaza)

                                    coAuthors.add(getScholarProfile(authors[j], page));
                                }
                                else {
                                    coAuthors.add(springJpaAuthorRepository.findByName(authors[j]).get(0)); //cioraneala
                                }
                            }
                            scrapedDocument.setAuthors(coAuthors);
                            page.navigate(currentUrl);
                            break;
                        case "Data publicării":
                            scrapedDocument.setPublicationDate(info);
                            break;
                        case "Jurnal":
                            scrapedDocument.setIssued(info);
                            break;
                        case "Volumul":
                            scrapedDocument.setVolume(info);
                            break;
                        case "Pagini":
                            scrapedDocument.setPages(info);
                            break;
                        case "Editor":
                            scrapedDocument.setPublisher(info);
                            break;
                        case "Descriere":
                            scrapedDocument.setDescription(info);
                            break;
                        case "Articole Google Academic":
                            String link = docInfo.get(i).locator("a").all().get(0).getAttribute("href");
                            if(link.startsWith("/")) {
                                StringBuilder stringBuilder = new StringBuilder(500);
                                stringBuilder.append("https://scholar.google.com");
                                stringBuilder.append(link);
                                scrapedDocument.setLink(stringBuilder.toString());
                                break;
                            }
                            scrapedDocument.setLink(link);
                            break;
                        default:
                            break;
                    }
                    i++;
                }

            }

            springJpaDocumentRepository.save(scrapedDocument);

            docNumber++;


//            wait(50000);

//            page.locator("#gsc_art").locator("#gsc_a_tw")



            System.out.println(page.title());

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

}