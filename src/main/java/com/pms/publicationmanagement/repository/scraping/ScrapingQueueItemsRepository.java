package com.pms.publicationmanagement.repository.scraping;

import com.pms.publicationmanagement.model.scraping.queue.ScrapingQueueItem;
import com.pms.publicationmanagement.model.scraping.enums.ScrapingQueueItemType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.UUID;

public interface ScrapingQueueItemsRepository extends JpaRepository<ScrapingQueueItem, Long>,
        PagingAndSortingRepository<ScrapingQueueItem, Long> {
    Page<ScrapingQueueItem> findAllByCreatedById(UUID createdById, PageRequest enqueuedTasksByPriorityPaged);

    Page<ScrapingQueueItem> findAllByTypeIn(List<ScrapingQueueItemType> types, PageRequest enqueuedTasksByPriorityPaged);
}
