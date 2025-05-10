package com.pms.publicationmanagement.repository;

import com.pms.publicationmanagement.model.scraping.DataSourceType;
import com.pms.publicationmanagement.model.scraping.ScrapedEntity;
import com.pms.publicationmanagement.model.scraping.ScrapedEntityType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ScrapedEntityRepository extends JpaRepository<ScrapedEntity, UUID> {

    Page<ScrapedEntity> findAllBySessionIdAndDataSource(UUID sessionId, DataSourceType dataSource, Pageable pageable);
}
