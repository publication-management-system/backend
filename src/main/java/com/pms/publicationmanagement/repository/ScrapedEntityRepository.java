package com.pms.publicationmanagement.repository;

import com.pms.publicationmanagement.model.scraping.DataSourceType;
import com.pms.publicationmanagement.model.scraping.ScrapedEntity;
import com.pms.publicationmanagement.model.scraping.ScrapedEntityType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface ScrapedEntityRepository extends JpaRepository<ScrapedEntity, UUID> {

    Page<ScrapedEntity> findAllBySessionIdAndDataSourceAndType(UUID sessionId,
                                                               DataSourceType dataSource,
                                                               ScrapedEntityType entityType,
                                                               Pageable pageable);

    @Query("select count(*) from ScrapedEntity se join ScrapingSession ss on ss.id = se.sessionId and ss.institutionId = :institutionId")
    Long countScrapedEntitiesByInstitution(String institutionId);
}
