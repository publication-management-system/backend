package com.pms.publicationmanagement.service.scraping;

import com.pms.publicationmanagement.model.scraping.DataSourceType;
import com.pms.publicationmanagement.model.scraping.ScrapedEntity;
import com.pms.publicationmanagement.model.scraping.ScrapedEntityType;
import com.pms.publicationmanagement.repository.ScrapedEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ScrapedEntityService {

    private final ScrapedEntityRepository scrapedEntityRepository;
    public List<ScrapedEntity> getAllScrapedEntity(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<ScrapedEntity> scraps = scrapedEntityRepository.findAll(pageable);
        return scraps.getContent();
    }

    public List<ScrapedEntity> getEntitiesFromSessionIdWithSource(UUID sessionId, DataSourceType dataSource, int pageNumber, int pageSize) {

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<ScrapedEntity> scraps = scrapedEntityRepository.findAllBySessionIdAndDataSource(sessionId, dataSource, pageable);
        return scraps.getContent();
    }
}
