package com.pms.publicationmanagement.service.scraping.dblp;

import com.pms.publicationmanagement.model.scraping.ScrapingSession;
import com.pms.publicationmanagement.service.scraping.dblp.dto.DblpScrapingDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class DblpScrapingService {

    @Value("${dblp-scraping-url}")
    private String dblpScrapingUrl;

    private final WebClient webClient;

    public void scrape(ScrapingSession session) {
        var scrapingDto = new DblpScrapingDto(session.getFirstName(), session.getLastName(), session.getId());

        webClient.post().uri(dblpScrapingUrl)
                .bodyValue(scrapingDto)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
