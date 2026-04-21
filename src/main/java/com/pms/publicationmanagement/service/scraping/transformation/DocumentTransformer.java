package com.pms.publicationmanagement.service.scraping.transformation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pms.publicationmanagement.model.profiling.Document;
import com.pms.publicationmanagement.model.scraping.DataSourceType;
import com.pms.publicationmanagement.model.scraping.payloads.DocumentPayload;
import com.pms.publicationmanagement.model.scraping.queue.ScrapingQueueItem;
import com.pms.publicationmanagement.model.scraping.queue.ScrapingQueueItemType;
import com.pms.publicationmanagement.repository.DocumentRepository;
import com.pms.publicationmanagement.repository.ScrapingQueueItemsRepository;
import com.pms.publicationmanagement.service.scraping.dto.ScrapingResponse;
import com.pms.publicationmanagement.service.scraping.dto.UrlPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentTransformer implements ITransformer {
    public static final Gson GSON = new GsonBuilder().disableHtmlEscaping().create();

    private final DocumentRepository documentRepository;
    private final ScrapingQueueItemsRepository scrapingQueueItemsRepository;

    @Override
    public void save(ScrapingQueueItem scrapingRequest, ScrapingResponse scrapingResponse, DataSourceType providerType) {
        var payload = new Gson().fromJson(scrapingResponse.getData(), DocumentPayload.class);

        var existingDocument = documentRepository.findExisting(scrapingResponse.getRefId(), payload.getProviderId())
                .orElse(null);

        if (existingDocument == null) {
            var newDocument = toDocument(payload, providerType);
            var saved = documentRepository.save(newDocument);
            enqueueNextItems(scrapingRequest, scrapingResponse, saved.getId().toString());
            return;
        }
    }

    private static Document toDocument(DocumentPayload payload, DataSourceType providerType) {
        return Document.builder()
                .id(UUID.randomUUID())
                .title(payload.getTitle())
                .description(payload.getDescription())
                .publicationDate(payload.getPublicationDate())
                .issue(payload.getIssue())
                .link(payload.getLink())
                .pages(payload.getPages())
                .publisher(payload.getPublisher())
                .googleScholarId(DataSourceType.GOOGLE_SCHOLAR == providerType ? payload.getProviderId() : null)
                .dblpId(DataSourceType.DBLP == providerType ? payload.getProviderId() : null)
                .wosId(DataSourceType.WEB_OF_SCIENCE == providerType ? payload.getProviderId() : null)
                .build();
    }

    private void updateExisting(Document document, DocumentPayload profile) {
        // @TODO
    }

    private void enqueueNextItems(ScrapingQueueItem item,
                                  ScrapingResponse response,
                                  String internalRefId) {
        var toSave = new ArrayList<ScrapingQueueItem>();
        for (var toEnqueue : response.getQueueItems()) {
            toSave.add(
                    ScrapingQueueItem.builder()
                            .type(ScrapingQueueItemType.valueOf(toEnqueue.getType()))
                            .payload(GSON.toJson(new UrlPayload(toEnqueue.getLink())))
                            .refId(internalRefId)
                            .scrapingLink(toEnqueue.getLink())
                            .createdAt(LocalDateTime.now())
                            .createdById(item.getCreatedById())
                            .createdByName(item.getCreatedByName())
                            .institutionId(item.getInstitutionId())
                            .priority(ScrapingQueueItemType.valueOf(toEnqueue.getType()).getPriority())
                            .provider(item.getProvider())
                            .build()
            );
        }

        scrapingQueueItemsRepository.saveAll(toSave);
    }
}
