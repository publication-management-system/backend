package com.pms.publicationmanagement.repository;

import com.pms.publicationmanagement.model.scraping.ScrapedEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ScrapedEntityRepository extends JpaRepository<ScrapedEntity, UUID> {
}
