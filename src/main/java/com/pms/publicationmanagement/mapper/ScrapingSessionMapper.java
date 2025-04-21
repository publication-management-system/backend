package com.pms.publicationmanagement.mapper;

import com.pms.publicationmanagement.dto.ScrapingSessionDto;
import com.pms.publicationmanagement.model.scraping.ScrapingSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ScrapingSessionMapper {

    public ScrapingSessionDto toScrapingSessionDto(ScrapingSession scrapingSession) {
        ScrapingSessionDto scrapingSessionDto = new ScrapingSessionDto();
        scrapingSessionDto.setId(scrapingSession.getId());
        scrapingSessionDto.setStatus(scrapingSession.getStatus());
        scrapingSessionDto.setUserId(scrapingSession.getUserId());
        scrapingSessionDto.setFirstName(scrapingSession.getFirstName());
        scrapingSessionDto.setLastName(scrapingSession.getLastName());
        scrapingSessionDto.setInstitutionId(scrapingSession.getInstitutionId());

        return scrapingSessionDto;
    }

    public List<ScrapingSessionDto> scrapingSessionDtoList(List<ScrapingSession> sessions) {
        List<ScrapingSessionDto> result = new ArrayList<>();
        for (ScrapingSession session : sessions) {
            result.add(toScrapingSessionDto(session));
        }

        return result;
    }
}
