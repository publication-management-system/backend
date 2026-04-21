package com.pms.publicationmanagement.model.scraping.queue;

import com.pms.publicationmanagement.model.scraping.DataSourceType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "scraping_queue_items")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ScrapingQueueItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String scrapingLink;

    @Enumerated(EnumType.STRING)
    private DataSourceType provider;

    @Enumerated(EnumType.STRING)
    private ScrapingQueueItemType type;

    private Long priority;

    private String payload;

    private LocalDateTime createdAt;

    private UUID createdById;
    private UUID institutionId;

    private String createdByName;

    private String refId;
}
