package com.pms.publicationmanagement.controllers.scraping;

import com.pms.publicationmanagement.dto.scraping.EnqueueScrapingRequestDto;
import com.pms.publicationmanagement.dto.scraping.ScrapingNextInQueueResponseDto;
import com.pms.publicationmanagement.dto.scraping.ScrapingStatusResponseDto;
import com.pms.publicationmanagement.dto.stats.ScrapingStatsDto;
import com.pms.publicationmanagement.model.scraping.queue.ScrapingQueueItem;
import com.pms.publicationmanagement.service.scraping.ScrapingService;
import com.pms.publicationmanagement.service.scraping.dto.ScrapingFailedItemDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/scraping")
public class ScrapingController {

    private final ScrapingService scrapingService;

    @PostMapping("/enqueue")
    public ScrapingStatusResponseDto enqueueScraping(@RequestBody @Valid EnqueueScrapingRequestDto enqueueScrapingRequestDto) {
        scrapingService.enqueueScrapingStartingFrom(
                enqueueScrapingRequestDto.getFirstName(),
                enqueueScrapingRequestDto.getLastName(),
                enqueueScrapingRequestDto.getInstitutionId(),
                enqueueScrapingRequestDto.getUserId(),
                enqueueScrapingRequestDto.getUserName()
        );

        return new ScrapingStatusResponseDto("Enqueued");
    }

    @GetMapping("/failed-items")
    public List<ScrapingFailedItemDto> getNextInQueueItems() {
        return scrapingService.getFailedItems(0, 10);
    }

    @PostMapping("/retry-failed-items")
    public void retryFailedItems() {
        scrapingService.retryFailedItems();
    }

    @GetMapping("/stats")
    public ScrapingStatsDto getStats() {
        return scrapingService.getStats();
    }
}
