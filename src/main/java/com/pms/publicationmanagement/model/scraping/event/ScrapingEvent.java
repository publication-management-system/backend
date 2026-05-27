package com.pms.publicationmanagement.model.scraping.event;

import com.pms.publicationmanagement.model.scraping.DataSourceType;
import com.pms.publicationmanagement.model.scraping.queue.ScrapingQueueItemType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "scraping_events")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScrapingEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID institutionId;

    @Enumerated(EnumType.STRING)
    private DataSourceType provider;

    @Enumerated(EnumType.STRING)
    private ScrapingQueueItemType type;

    private LocalDateTime scrapedAt;

    private boolean success;
}
