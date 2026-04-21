package com.pms.publicationmanagement.controllers;

import com.pms.publicationmanagement.dto.scraping.EnqueueScrapingRequestDto;
import com.pms.publicationmanagement.dto.scraping.ScrapingNextInQueueResponseDto;
import com.pms.publicationmanagement.dto.scraping.ScrapingStatusResponseDto;
import com.pms.publicationmanagement.model.scraping.queue.ScrapingQueueItem;
import com.pms.publicationmanagement.service.scraping.ScrapingService2;
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

    private final ScrapingService2 scrapingService2;

    @PostMapping("/enqueue")
    public ScrapingStatusResponseDto enqueueScraping(@RequestBody @Valid EnqueueScrapingRequestDto enqueueScrapingRequestDto) {
        scrapingService2.enqueueScrapingStartingFrom(
                enqueueScrapingRequestDto.getFirstName(),
                enqueueScrapingRequestDto.getLastName(),
                enqueueScrapingRequestDto.getInstitutionId(),
                enqueueScrapingRequestDto.getUserId(),
                enqueueScrapingRequestDto.getUserName()
        );

        return new ScrapingStatusResponseDto("Enqueued");
    }

    @PostMapping
    public ScrapingStatusResponseDto runScraping() {
        scrapingService2.runEnqueuedTasks();

        return new ScrapingStatusResponseDto("Running");
    }

    @GetMapping("/next-in-queue")
    public ScrapingNextInQueueResponseDto getNextInQueueItems(@RequestParam UUID userId) {
        Page<ScrapingQueueItem> nextEnqueuedItemsByUserId = scrapingService2.getNextEnqueuedItemsByUserId(userId);

        return new ScrapingNextInQueueResponseDto(
                nextEnqueuedItemsByUserId.stream().toList(),
                nextEnqueuedItemsByUserId.getTotalElements()
        );
    }
}
