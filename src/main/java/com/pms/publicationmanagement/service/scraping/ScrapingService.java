package com.pms.publicationmanagement.service.scraping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.pms.publicationmanagement.dto.stats.ScrapingCountsByProvider;
import com.pms.publicationmanagement.dto.stats.ScrapingItemByMinute;
import com.pms.publicationmanagement.dto.stats.ScrapingStatsDto;
import com.pms.publicationmanagement.model.scraping.enums.DataSourceType;
import com.pms.publicationmanagement.model.scraping.queue.ScrapingFailedQueueItem;
import com.pms.publicationmanagement.model.scraping.queue.ScrapingQueueItem;
import com.pms.publicationmanagement.model.scraping.enums.ScrapingQueueItemType;
import com.pms.publicationmanagement.repository.scraping.ScrapingEventRepository;
import com.pms.publicationmanagement.repository.scraping.ScrapingFailedQueueItemsRepository;
import com.pms.publicationmanagement.repository.scraping.ScrapingQueueItemsRepository;
import com.pms.publicationmanagement.service.scraping.dto.FindAuthorPayload;
import com.pms.publicationmanagement.service.scraping.dto.ScrapingFailedItemDto;
import com.pms.publicationmanagement.service.scraping.dto.ScrapingPayload;
import com.pms.publicationmanagement.service.scraping.dto.ScrapingResponse;
import com.pms.publicationmanagement.service.scraping.transformation.TransformationOrchestrator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScrapingService {

    private static final Long MAX_AUTHORS_IN_QUEUE = 3L;

    @Value("${scraping.service.run-api-path}")
    private String scrapingRunApiPath;

    private final ScrapingQueueItemsRepository scrapingQueueItemsRepository;
    private final WebClient scrapingServiceWebClient;
    private final TaskExecutor scrapingTaskExecutor;
    private final TransformationOrchestrator transformationOrchestrator;
    private final ScrapingEventRepository scrapingEventRepository;
    private final ScrapingFailedQueueItemsRepository scrapingFailedQueueItemsRepository;
    private final ObjectMapper objectMapper;


    @Scheduled(fixedRate = 60000)
    public void runScrapingActions() {
        var enqueuedTasksByPriorityPaged = PageRequest
                .of(0, 30, Sort.by("priority").ascending());

        var scrapingItems = scrapingQueueItemsRepository.findAll(enqueuedTasksByPriorityPaged);

        if (scrapingItems == null || scrapingItems.getContent() == null) {
            log.info("No queued documents");
            return;
        }

        List<ScrapingQueueItem> finalItems = new ArrayList<>();
        long authorCounter = 0L;
        for (var finalItem : scrapingItems.getContent()) {
            if ((finalItem.getType() == ScrapingQueueItemType.FIND_AUTHOR
                    || finalItem.getType() == ScrapingQueueItemType.FIND_AUTHOR_DBLP)) {
                if (authorCounter < MAX_AUTHORS_IN_QUEUE) {
                    authorCounter++;
                    finalItems.add(finalItem);
                }
            } else {
                finalItems.add(finalItem);
            }
        }

        for (var item : finalItems) {
            scrapingTaskExecutor.execute(() -> callScrapingService(item));
        }
    }

    public void retryFailedItems() {
        var enqueuedTasksByPriorityPaged = PageRequest
                .of(0, 100);

        var failedScrapingItems = scrapingFailedQueueItemsRepository.findAll(enqueuedTasksByPriorityPaged);

        if (failedScrapingItems == null || failedScrapingItems.getContent() == null) {
            log.info("No queued documents");
            return;
        }
        var toSave = new ArrayList<ScrapingQueueItem>();

        for (var failedItem : failedScrapingItems.getContent()) {
            try {
                var deserialisedPayload = objectMapper.readValue(failedItem.getPayload(), ScrapingQueueItem.class);
                deserialisedPayload.setId(null);

                toSave.add(deserialisedPayload);
            } catch (JsonProcessingException e) {
                log.warn("Could not deserialize failed payload", e);
            }
        }

        scrapingQueueItemsRepository.saveAll(toSave);
        scrapingFailedQueueItemsRepository.deleteAll(failedScrapingItems);
    }

    public void enqueueScrapingStartingFrom(String firstName, String lastName, UUID institutionId, UUID userId,
                                            String userName) {
        var authorItemQueueGS = ScrapingQueueItem.builder()
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

        var authorItemQueueDBLP = ScrapingQueueItem.builder()
                .type(ScrapingQueueItemType.FIND_AUTHOR_DBLP)
                .priority(ScrapingQueueItemType.FIND_AUTHOR_DBLP.getPriority())
                .payload(new Gson().toJson(new FindAuthorPayload(firstName, lastName)))
                .createdById(userId)
                .provider(DataSourceType.DBLP)
                .institutionId(institutionId)
                .createdByName(userName)
                .createdAt(LocalDateTime.now())
                .refId(UUID.randomUUID().toString())
                .build();

        scrapingQueueItemsRepository.saveAll(List.of(authorItemQueueGS, authorItemQueueDBLP));
    }

    public List<ScrapingFailedItemDto> getFailedItems(Integer pageNumber, Integer pageSize) {
        var enqueuedTasksByPriorityPaged = PageRequest
                .of(pageNumber, pageSize);

        var failedItems = scrapingFailedQueueItemsRepository.findAll(enqueuedTasksByPriorityPaged);

        if (failedItems == null) {
            return Collections.emptyList();
        }

        var response = new ArrayList<ScrapingFailedItemDto>();
        for (var failedItem : failedItems) {
            try {
                var scrapingQueueItem = objectMapper.readValue(failedItem.getPayload(), ScrapingQueueItem.class);
                var toAdd = ScrapingFailedItemDto.builder()
                        .id(failedItem.getId().toString())
                        .type(scrapingQueueItem.getType().name())
                        .dataSource(scrapingQueueItem.getProvider())
                        .details(scrapingQueueItem.getType() == ScrapingQueueItemType.FIND_AUTHOR
                                ? scrapingQueueItem.getPayload() : scrapingQueueItem.getScrapingLink())
                        .build();

                response.add(toAdd);
            } catch (JsonProcessingException e) {
                log.error("Could not process failed payload", e);
            }
        }

        return response;
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
            log.info("Scraping response {}", response);
            transformationOrchestrator.transformScraping(request, response);
            scrapingQueueItemsRepository.deleteById(request.getId());
        } catch (Exception ex) {
            log.warn("Scraping might've failed req= {} resp =", request, ex);
            ScrapingFailedQueueItem failedItem = null;
            try {
                failedItem = ScrapingFailedQueueItem.builder()
                        .failedAt(LocalDateTime.now())
                        .payload(objectMapper.writeValueAsString(request))
                        .build();
                scrapingFailedQueueItemsRepository.save(failedItem);
            } catch (JsonProcessingException e) {
                log.error("Could not save failed item", e);
            }


            scrapingQueueItemsRepository.deleteById(request.getId());
        }
    }

    public ScrapingStatsDto getStats() {
        var countsByMinutes = scrapingEventRepository.getScrapedEventsByMinute(LocalDateTime.now().minusMinutes(10));
        var countsByProvider = scrapingEventRepository.getCountsByProvider();

        var lastMinutes = countsByMinutes.stream()
                .map(c -> new ScrapingItemByMinute(c.getMinute(), c.getCount()))
                .toList();

        var countsByProviderList = countsByProvider.stream()
                .map(cbp -> new ScrapingCountsByProvider(cbp.getProvider(), cbp.getCount()))
                .toList();

        return new ScrapingStatsDto(lastMinutes, countsByProviderList);
    }
}