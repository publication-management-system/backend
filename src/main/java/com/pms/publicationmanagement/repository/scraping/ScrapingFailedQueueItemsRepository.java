package com.pms.publicationmanagement.repository.scraping;

import com.pms.publicationmanagement.model.scraping.queue.ScrapingFailedQueueItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScrapingFailedQueueItemsRepository extends JpaRepository<ScrapingFailedQueueItem, Long> {
}
