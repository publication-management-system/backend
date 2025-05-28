package com.pms.publicationmanagement.service.scraping;

import com.pms.publicationmanagement.dto.ScrapingRequestDto;
import com.pms.publicationmanagement.dto.ScrapingSessionDto;
import com.pms.publicationmanagement.mapper.ScrapingSessionMapper;
import com.pms.publicationmanagement.model.scraping.ScrapedEntity;
import com.pms.publicationmanagement.model.scraping.ScrapingSession;
import com.pms.publicationmanagement.model.scraping.ScrapingSessionStatus;
import com.pms.publicationmanagement.repository.ScrapedEntityRepository;
import com.pms.publicationmanagement.repository.ScrapingSessionRepository;
import com.pms.publicationmanagement.service.scraping.dblp.DblpScrapingService;
import com.pms.publicationmanagement.service.scraping.googlescholar.GoogleScholarScrapingService;
import lombok.RequiredArgsConstructor;
import org.jobrunr.scheduling.JobScheduler;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WebScrapingService {

    private final ScrapingSessionRepository scrapingSessionRepository;

    private final ScrapingSessionMapper scrapingSessionMapper;

    private final GoogleScholarScrapingService googleScholarScrapingService;

    private final DblpScrapingService dblpScrapingService;

    public UUID runScraping(ScrapingRequestDto scrapingRequestDto) {
        ScrapingSession scrapingSession = createNewScrapingSession(scrapingRequestDto);
//        googleScholarScrapingService.scrape(scrapingSession);
        dblpScrapingService.scrape(scrapingSession);

        return scrapingSession.getId();
    }


    public List<ScrapingSessionDto> findAll(String institutionId) {
        return scrapingSessionMapper.scrapingSessionDtoList(
                scrapingSessionRepository.findAllByInstitutionId(institutionId)
        );
    }

    public ScrapingSessionDto findById(UUID sessionId) {
        ScrapingSession session = scrapingSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        return scrapingSessionMapper.toScrapingSessionDto(session);
    }

    private ScrapingSession createNewScrapingSession(ScrapingRequestDto scrapingRequestDto) {
        ScrapingSession scrapingSession = new ScrapingSession();
        scrapingSession.setFirstName(scrapingRequestDto.getFirstName());
        scrapingSession.setLastName(scrapingRequestDto.getLastName());
        scrapingSession.setStatus(ScrapingSessionStatus.IN_PROGRESS.name());
        scrapingSession.setUserId(scrapingRequestDto.getUserId());
        scrapingSession.setInstitutionId(scrapingRequestDto.getInstitutionId());
        scrapingSession.setCreatedBy(scrapingRequestDto.getUserName());

        return scrapingSessionRepository.save(scrapingSession);
    }
}
