package com.pms.publicationmanagement.services;

import com.microsoft.playwright.*;
import com.pms.publicationmanagement.model.Author;
import com.pms.publicationmanagement.model.Citation;
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
        if(profileChecker.isEmpty()) { //pt cine nu are profil
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
        String institutionPage;
        if(!authorProfile.locator("#gsc_prf_ivh").locator("a").all().isEmpty()) {
            institutionPage = authorProfile.locator("#gsc_prf_ivh").locator("a").all().get(0).getAttribute("href");
        }
        else {
            institutionPage = authorProfile.locator("#gsc_prf_ivh").innerText();
        }

        scrapedAuthor.setName(authorName);

        if (roleAndInstitution.length > 1) {
            scrapedAuthor.setRole(roleAndInstitution[0]);
            scrapedAuthor.setInstitution(roleAndInstitution[1]);
        } else {
            scrapedAuthor.setInstitution(roleAndInstitution[0]);
        }

        scrapedAuthor.setInstitutionMail(institutionPage);

        if(springJpaAuthorRepository.findByName(name).isEmpty()) {
            springJpaAuthorRepository.save(scrapedAuthor);
        }
        return scrapedAuthor;
    }

    public void addPageOfCitations(List<Citation> citationList, Page citationPage) {
        citationPage.waitForSelector("#gs_res_ccl_mid");
        List<Locator> citations = citationPage.locator("#gs_res_ccl_mid").locator("[class=\"gs_r gs_or gs_scl\"]").all();
        for(Locator citation : citations) {
            String citationTitle, citationLink;
            if(citation.locator("h3").locator("a").all().isEmpty()) {
                citationTitle = citation.locator("h3").locator("span").all().get(3).innerText(); //doamne ajuta
                citationLink = citation.locator("[class=\"gs_a\"]").innerText(); //sunt defapt autori in cazul asta
            }
            else {
                citationTitle = citation.locator("h3").locator("a").innerText();
                citationLink = citation.locator("h3").locator("a").getAttribute("href");
            }
            Citation citatoinToBeAdded = new Citation();
            citatoinToBeAdded.setLink(citationLink);
            citatoinToBeAdded.setTitle(citationTitle);
            citationList.add(citatoinToBeAdded);
            //mai trebuie facut un document din citare (DANK)
        }
    }

    public String addScholarLinkPrefix(String link) {
        StringBuilder stringBuilder = new StringBuilder(500);
        stringBuilder.append("https://scholar.google.com");
        stringBuilder.append(link);
        return stringBuilder.toString();
    }


    public void scrape(String name) {
        List<String> args = new ArrayList<>();
        args.add("-private");
        try (Playwright playwright = Playwright.create();
             Browser browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(false).setArgs(args))) {
            Page page = browser.newPage();

            getScholarProfile(name, page);
            

            Locator showMoreButton = page.locator("#gsc_bpf_more");
//            int docs = page.locator("#gsc_a_tw").locator("tbody").locator("tr").all().size();  //EN DASHES

            List<String> docLink = new ArrayList<>();
            List<String> docTitle = new ArrayList<>();

            int currentDocIndex = 0;
            do {
                if (showMoreButton.getAttribute("disabled") == null) {
                    showMoreButton.click();
                }

                int numberOfDocs = page.locator("#gsc_a_tw").locator("tbody").locator("tr").all().size();

                List<Locator> documents = page.locator("#gsc_a_tw").locator("tbody").locator("tr").all();

                for (int index = currentDocIndex; index < numberOfDocs; ++index) {
                    String link = documents.get(index).locator("[class=\"gsc_a_t\"]").all().get(0).locator("a").getAttribute("href");
                    if(link.startsWith("/")) {
                        link = addScholarLinkPrefix(link);
                    }
                    docLink.add(link);
                    docTitle.add(documents.get(index).locator("[class=\"gsc_a_t\"]").all().get(0).locator("a").innerText());
                }
                currentDocIndex = numberOfDocs - 1;
            } while (showMoreButton.getAttribute("disabled") == null);

            int currentDoc = 0;

            //poate trebuie page.url() aici?

            while(currentDoc<currentDocIndex) {

                Document scrapedDocument = new Document();

//                Locator docTable = page.locator("#gsc_a_b");
//
//                String docName = docTable.locator("tr").all().get(currentDoc).locator("[class=\"gsc_a_t\"]").all().get(0).locator("a").innerText();
//
//                String docsUrl = page.url();



                if (springJpaDocumentRepository.findByTitle(docTitle.get(currentDoc)).isEmpty()) {

                    scrapedDocument.setTitle(docTitle.get(currentDoc));

//                    docTable.locator("tr").all().get(currentDoc).
//                            locator("[class=\"gsc_a_t\"]").all().get(0).locator("a").click(); //am adaugat .locator("a"). dar inainte mergea fara?

                    page.navigate(docLink.get(currentDoc));
                    page.waitForSelector("#gsc_oci_table");
                    List<Locator> docDetails = page.locator("#gsc_oci_table").locator("[class=\"gs_scl\"]")
                            .locator("[class=\"gsc_oci_field\"]").all();


                    //ceva aici?

                    List<Locator> docInfo = page.locator("#gsc_oci_table").locator("[class=\"gs_scl\"]").locator("[class=\"gsc_oci_value\"]").all();

                    int i = 0;
                    String detail, info;

                    while (i < docDetails.size()) {
                        detail = docDetails.get(i).innerText();
                        info = docInfo.get(i).innerText();

                        switch (detail) {
                            case "Authors":
                                String[] authors = info.split(",");
                                List<Author> coAuthors = new ArrayList<>();
                                String currentUrl = page.url();
                                for (int j = 0; j < authors.length; j++) {
                                    authors[j] = authors[j].trim();
                                    if (springJpaAuthorRepository.findByName(authors[j]).isEmpty()) { //cazuri cand numele are diacritice(in bd nu se salveaza)

                                        coAuthors.add(getScholarProfile(authors[j], page));
                                    } else {
                                        coAuthors.add(springJpaAuthorRepository.findByName(authors[j]).get(0)); //cioraneala
                                    }
                                }
                                scrapedDocument.setAuthors(coAuthors);
                                page.navigate(currentUrl);
                                break;
                            case "Publication date":
                                scrapedDocument.setPublicationDate(info);
                                break;
                            case "Journal":
                                scrapedDocument.setIssued(info);
                                break;
                            case "Book":
                                scrapedDocument.setIssued(info);
                                break;
                            case "Conference":
                                scrapedDocument.setIssued(info);
                                break;
                            case "Source":
                                scrapedDocument.setIssued(info);
                                break;
                            case "Volume":
                                scrapedDocument.setVolume(info);
                                break;
                            case "Issue":
                                scrapedDocument.setIssue(info);
                                break;
                            case "Pages":
                                scrapedDocument.setPages(info);
                                break;
                            case "Publisher":
                                scrapedDocument.setPublisher(info);
                                break;
                            case "Description":
                                scrapedDocument.setDescription(info);
                                break;
                            case "Scholar articles":
                                String link = docInfo.get(i).locator("a").all().get(0).getAttribute("href");
                                if (link.startsWith("/")) {
                                    scrapedDocument.setLink(addScholarLinkPrefix(link));
                                    break;
                                }
                                scrapedDocument.setLink(link);
                                break;
                            case "Total citations":
                                String docDetailUrl = page.url();
                                List<Citation> citationList = new ArrayList<>();
                                docInfo.get(i).locator("a").all().get(0).click();
                                addPageOfCitations(citationList, page);
                                if(citationList.size() == 10) {
                                    List<Locator> nextPage = page.locator("#gs_n").locator("tr").locator("td").all()
                                            .get(page.locator("#gs_n").locator("tr").locator("td").all().size() - 1).
                                            locator("a").all();
                                    while(!nextPage.isEmpty()) {
                                        nextPage.get(0).click();
                                        if(page.locator("#gs_n").locator("tr").locator("td").all().size() == 0) {
                                            System.out.println("macac");
                                        }
                                        nextPage = page.locator("#gs_n").locator("tr").locator("td").all()
                                                .get(page.locator("#gs_n").locator("tr").locator("td").all().size() - 1)
                                                .locator("a").all();
                                        addPageOfCitations(citationList, page);
                                    }
                                }
                                scrapedDocument.setCitedIn(citationList);
                                page.navigate(docDetailUrl);
                                break;
                            default:
                                break;
                        }
                        i++;
                    }

                    springJpaDocumentRepository.save(scrapedDocument);
                }

                currentDoc++;
//                page.navigate(docsUrl);

            }

//            wait(50000);

//            page.locator("#gsc_art").locator("#gsc_a_tw")



            System.out.println(page.title());

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

}