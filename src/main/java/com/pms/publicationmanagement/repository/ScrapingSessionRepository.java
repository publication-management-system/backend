package com.pms.publicationmanagement.repository;

import com.pms.publicationmanagement.model.scraping.ScrapingSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ScrapingSessionRepository extends JpaRepository<ScrapingSession, UUID> {
}
