package com.pms.publicationmanagement.repository.scraping;

import com.pms.publicationmanagement.model.scraping.event.ScrapingEvent;
import com.pms.publicationmanagement.repository.projections.ScrapingCountsByMinute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ScrapingEventRepository extends JpaRepository<ScrapingEvent, Long> {
    @Query(value = """
        SELECT
            STR_TO_DATE(
                    DATE_FORMAT(scraped_at, '%Y-%m-%d %H:%i:00'),
                    '%Y-%m-%d %H:%i:%s'
            ) AS minute,
            COUNT(*) AS count
        FROM scraping_events
        WHERE scraped_at >= :from
        GROUP BY minute
        ORDER BY minute
    """, nativeQuery = true)
    List<ScrapingCountsByMinute> getScrapedEventsByMinute(
            @Param("from") LocalDateTime from
    );
}
