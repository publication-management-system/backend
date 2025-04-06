package com.pms.publicationmanagement.service.scraping;

import com.pms.publicationmanagement.dto.ScrapingRequestDto;
import com.pms.publicationmanagement.model.scraping.ScrapingSession;
import com.pms.publicationmanagement.model.scraping.ScrapingSessionStatus;
import com.pms.publicationmanagement.repository.ScrapingSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WebScrapingService {

    private final ScrapingSessionRepository scrapingSessionRepository;

    private final IWebScrapingProfiling googleScholarProfilingService;

    private final  IWebScrapingProfiling dblpService;

    private final IWebScrapingProfiling scopusService;

    private final IWebScrapingProfiling wosService;

    public UUID runScraping(ScrapingRequestDto scrapingRequestDto) {
        ScrapingSession scrapingSession = createNewScrapingSession(scrapingRequestDto);
        googleScholarProfilingService.scrape(scrapingSession);
//        dblpService.scrape(scrapingSession);
//        scopusService.scrape(scrapingSession);
//        wosService.scrape(scrapingSession);

        return scrapingSession.getId();
    }

    private ScrapingSession createNewScrapingSession(ScrapingRequestDto scrapingRequestDto) {
        ScrapingSession scrapingSession = new ScrapingSession();
        scrapingSession.setFirstName(scrapingRequestDto.getFirstName());
        scrapingSession.setLastName(scrapingRequestDto.getLastName());
        scrapingSession.setStatus(ScrapingSessionStatus.IN_PROGRESS.name());
        scrapingSession.setUserId(Long.parseLong(scrapingRequestDto.getUserId()));
        scrapingSession.setInstitutionId(scrapingSession.getInstitutionId());

        return scrapingSessionRepository.save(scrapingSession);
    }
}
