package com.pms.publicationmanagement.service.stats;

import com.pms.publicationmanagement.dto.stats.KeyValStatsDto;
import com.pms.publicationmanagement.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StatsService {

    private final UserRepository userRepository;

    private final InstitutionRepository institutionRepository;

    private final ProjectRepository projectRepository;

    private final ScrapingSessionRepository scrapingSessionRepository;
    private final ScrapedEntityRepository scrapedEntityRepository;

    public List<KeyValStatsDto> getInstitutionStats(UUID institutionId) {
        List<KeyValStatsDto> keyValStatsDtos = new ArrayList<>();

        keyValStatsDtos.add(new KeyValStatsDto("Number of members",
                userRepository.countByInstitutionId(institutionId).toString()));

        keyValStatsDtos.add(new KeyValStatsDto("Scraping sessions",
                scrapingSessionRepository.countByInstitutionId(institutionId.toString()).toString()));

        keyValStatsDtos.add(new KeyValStatsDto("Scraped articles",
                scrapedEntityRepository.countScrapedEntitiesByInstitution(institutionId.toString()).toString()));


        return keyValStatsDtos;
    }

}
