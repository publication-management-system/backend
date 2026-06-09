package com.pms.publicationmanagement.model.scraping.queue;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "scraping_failed_queue_items")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ScrapingFailedQueueItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime failedAt;

    private String payload;
}
