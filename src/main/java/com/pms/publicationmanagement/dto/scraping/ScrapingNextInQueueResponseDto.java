package com.pms.publicationmanagement.dto.scraping;

import com.pms.publicationmanagement.model.scraping.queue.ScrapingQueueItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScrapingNextInQueueResponseDto {
    private List<ScrapingQueueItem> nextInQueue;

    private Long totalInQueue;
}
