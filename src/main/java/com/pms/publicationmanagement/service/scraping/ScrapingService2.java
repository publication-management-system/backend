package com.pms.publicationmanagement.service.scraping;

import com.google.gson.Gson;
import com.pms.publicationmanagement.dto.stats.ScrapingItemByMinute;
import com.pms.publicationmanagement.dto.stats.ScrapingStatsDto;
import com.pms.publicationmanagement.model.scraping.DataSourceType;
import com.pms.publicationmanagement.model.scraping.queue.ScrapingQueueItem;
import com.pms.publicationmanagement.model.scraping.queue.ScrapingQueueItemType;
import com.pms.publicationmanagement.repository.scraping.ScrapingEventRepository;
import com.pms.publicationmanagement.repository.scraping.ScrapingQueueItemsRepository;
import com.pms.publicationmanagement.service.scraping.dto.FindAuthorPayload;
import com.pms.publicationmanagement.service.scraping.dto.ScrapingPayload;
import com.pms.publicationmanagement.service.scraping.dto.ScrapingResponse;
import com.pms.publicationmanagement.service.scraping.transformation.TransformationOrchestrator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScrapingService2 {
    @Value("${scraping.service.run-api-path}")
    private String scrapingRunApiPath;

    private final ScrapingQueueItemsRepository scrapingQueueItemsRepository;
    private final WebClient scrapingServiceWebClient;
    private final TaskExecutor scrapingTaskExecutor;
    private final TransformationOrchestrator transformationOrchestrator;
    private final ScrapingEventRepository scrapingEventRepository;

    public void enqueueScrapingStartingFrom(String firstName, String lastName, UUID institutionId, UUID userId,
                                            String userName) {
        var scrapingItemQueue = ScrapingQueueItem.builder()
                .type(ScrapingQueueItemType.FIND_AUTHOR)
                .priority(ScrapingQueueItemType.FIND_AUTHOR.getPriority())
                .payload(new Gson().toJson(new FindAuthorPayload(firstName, lastName)))
                .createdById(userId)
                .provider(DataSourceType.GOOGLE_SCHOLAR)
                .institutionId(institutionId)
                .createdByName(userName)
                .createdAt(LocalDateTime.now())
                .refId(UUID.randomUUID().toString())
                .build();

        scrapingQueueItemsRepository.save(scrapingItemQueue);
    }

    public Page<ScrapingQueueItem> getNextEnqueuedItemsByUserId(UUID userId) {
        var enqueuedTasksByPriorityPaged = PageRequest
                .of(0, 10, Sort.by("priority").ascending());

        return scrapingQueueItemsRepository.findAllByCreatedById(userId, enqueuedTasksByPriorityPaged);
    }

    @Scheduled(fixedRate = 30000)
    public void runDocumentTasks() {
        var enqueuedTasksByPriorityPaged = PageRequest
                .of(0, 100, Sort.by("priority").ascending());

        var scrapingItems = scrapingQueueItemsRepository.findAllByTypeIn(
                List.of(ScrapingQueueItemType.FIND_AUTHOR, ScrapingQueueItemType.DOCUMENT, ScrapingQueueItemType.CITATIONS_GS),
                enqueuedTasksByPriorityPaged);

        if (scrapingItems == null || scrapingItems.getContent() == null) {
            log.info("No queued documents");
            return;
        }

        for (var item : scrapingItems.getContent()) {
            scrapingTaskExecutor.execute(() -> callScrapingService(item));
        }
    }

    private void callScrapingService(ScrapingQueueItem request) {
        try {
            var response = scrapingServiceWebClient.post()
                    .uri(uriBuilder -> uriBuilder.path(scrapingRunApiPath)
                            .queryParam("actionType", request.getType().name())
                            .build())
                    .bodyValue(new ScrapingPayload(request.getPayload(), request.getRefId()))
                    .retrieve()
                    .bodyToMono(ScrapingResponse.class)
                    .block();

            transformationOrchestrator.transformScraping(request, response);
        } catch (Exception ex) {
            log.warn("Scraping might've failed");
        }
    }

    public ScrapingStatsDto getStats() {
        var countsByMinutes = scrapingEventRepository.getScrapedEventsByMinute(LocalDateTime.now().minusMinutes(10));

        var lastMinutes = countsByMinutes.stream()
                .map(c -> new ScrapingItemByMinute(c.getMinute(), c.getCount()))
                .toList();

        return new ScrapingStatsDto(lastMinutes);
    }
}
