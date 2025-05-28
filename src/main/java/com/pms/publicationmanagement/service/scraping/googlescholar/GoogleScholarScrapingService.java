package com.pms.publicationmanagement.service.scraping.googlescholar;

import com.pms.publicationmanagement.model.scraping.ScrapingSession;
import com.pms.publicationmanagement.service.scraping.googlescholar.dto.GoogleScholarScrapingDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class GoogleScholarScrapingService {

    @Value("${google-scholar-scraping-url}")
    private String googleScholarScrapingUrl;

    private final WebClient webClient;

    public void scrape(ScrapingSession session) {
        var scrapingDto = new GoogleScholarScrapingDto(session.getFirstName(), session.getLastName(), session.getId());

        webClient.post().uri(googleScholarScrapingUrl)
                .bodyValue(scrapingDto)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
