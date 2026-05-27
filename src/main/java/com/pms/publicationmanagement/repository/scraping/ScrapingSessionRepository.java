package com.pms.publicationmanagement.repository.scraping;

import com.pms.publicationmanagement.model.scraping.ScrapingSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ScrapingSessionRepository extends JpaRepository<ScrapingSession, UUID> {
    List<ScrapingSession> findAllByInstitutionId(String institutionId);

    Long countByInstitutionId(String institutionId);
}
