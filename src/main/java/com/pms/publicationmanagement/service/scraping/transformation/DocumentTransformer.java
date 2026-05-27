package com.pms.publicationmanagement.service.scraping.transformation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pms.publicationmanagement.model.profiling.Author;
import com.pms.publicationmanagement.model.profiling.Document;
import com.pms.publicationmanagement.model.scraping.DataSourceType;
import com.pms.publicationmanagement.model.scraping.payloads.DocumentPayload;
import com.pms.publicationmanagement.model.scraping.queue.ScrapingQueueItem;
import com.pms.publicationmanagement.model.scraping.queue.ScrapingQueueItemType;
import com.pms.publicationmanagement.repository.AuthorRepository;
import com.pms.publicationmanagement.repository.DocumentRepository;
import com.pms.publicationmanagement.repository.scraping.ScrapingQueueItemsRepository;
import com.pms.publicationmanagement.service.scraping.dto.ScrapingResponse;
import com.pms.publicationmanagement.service.scraping.dto.UrlPayload;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentTransformer implements ITransformer {
    public static final Gson GSON = new GsonBuilder().disableHtmlEscaping().create();

    private final DocumentRepository documentRepository;
    private final AuthorRepository authorRepository;
    private final ScrapingQueueItemsRepository scrapingQueueItemsRepository;

    @Override
    @Transactional
    public void save(
            ScrapingQueueItem scrapingRequest,
            ScrapingResponse scrapingResponse,
            DataSourceType providerType
    ) {
        var payload = GSON.fromJson(scrapingResponse.getData(), DocumentPayload.class);


        log.info(
                "Mapped DocumentPayload title={}, issued={}, volume={}, pages={}, publisher={}, providerId={}, link={}",
                payload.getTitle(),
                payload.getIssued(),
                payload.getVolume(),
                payload.getPages(),
                payload.getPublisher(),
                payload.getProviderId(),
                payload.getLink()
        );

        UUID authorId = UUID.fromString(scrapingRequest.getRefId());

        var author = authorRepository.findById(authorId)
                .orElseThrow(() -> new IllegalStateException("Author not found: " + authorId));

        var existingDocument = findExistingDocument(providerType, payload.getProviderId()).orElse(null);

        Document document;

        if (existingDocument == null) {
            document = documentRepository.save(toDocument(payload, providerType));
        } else {
            updateExisting(existingDocument, payload);
            document = existingDocument;
        }

        linkAuthorToDocument(document, author);

        enqueueNextItems(scrapingRequest, scrapingResponse, document.getId().toString());
    }

    private Optional<Document> findExistingDocument(DataSourceType providerType, String providerId) {
        if (providerId == null || providerId.isBlank()) {
            return Optional.empty();
        }

        return switch (providerType) {
            case GOOGLE_SCHOLAR -> documentRepository.findByGoogleScholarId(providerId);
            case DBLP -> documentRepository.findByDblpId(providerId);
            case WEB_OF_SCIENCE -> documentRepository.findByWosId(providerId);
            default -> throw new RuntimeException("Invalid provider type");
        };
    }

    private static Document toDocument(DocumentPayload payload, DataSourceType providerType) {
        return Document.builder()
                .title(payload.getTitle())
                .description(payload.getDescription())
                .publicationDate(payload.getPublicationDate())
                .issued(payload.getIssued())
                .volume(payload.getVolume())
                .issue(payload.getIssue())
                .link(payload.getLink())
                .pages(payload.getPages())
                .publisher(payload.getPublisher())
                .googleScholarId(DataSourceType.GOOGLE_SCHOLAR == providerType ? payload.getProviderId() : null)
                .dblpId(DataSourceType.DBLP == providerType ? payload.getProviderId() : null)
                .wosId(DataSourceType.WEB_OF_SCIENCE == providerType ? payload.getProviderId() : null)
                .build();
    }

    private void updateExisting(Document document, DocumentPayload payload) {
        if (document.getTitle() == null && payload.getTitle() != null) {
            document.setTitle(payload.getTitle());
        }

        if (document.getDescription() == null && payload.getDescription() != null) {
            document.setDescription(payload.getDescription());
        }

        if (document.getPublicationDate() == null && payload.getPublicationDate() != null) {
            document.setPublicationDate(payload.getPublicationDate());
        }

        if (document.getIssued() == null && payload.getIssued() != null) {
            document.setIssued(payload.getIssued());
        }

        if (document.getVolume() == null && payload.getVolume() != null) {
            document.setVolume(payload.getVolume());
        }

        if (document.getIssue() == null && payload.getIssue() != null) {
            document.setIssue(payload.getIssue());
        }

        if (document.getLink() == null && payload.getLink() != null) {
            document.setLink(payload.getLink());
        }

        if (document.getPages() == null && payload.getPages() != null) {
            document.setPages(payload.getPages());
        }

        if (document.getPublisher() == null && payload.getPublisher() != null) {
            document.setPublisher(payload.getPublisher());
        }
    }

    private void enqueueNextItems(
            ScrapingQueueItem item,
            ScrapingResponse response,
            String internalDocumentId
    ) {
        var toSave = new ArrayList<ScrapingQueueItem>();

        for (var toEnqueue : response.getQueueItems()) {
            toSave.add(
                    ScrapingQueueItem.builder()
                            .type(ScrapingQueueItemType.valueOf(toEnqueue.getType()))
                            .payload(GSON.toJson(new UrlPayload(toEnqueue.getLink())))
                            .refId(internalDocumentId)
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

    private void linkAuthorToDocument(Document document, Author author) {
        if (document.getAuthors() == null) {
            document.setAuthors(new ArrayList<>());
        }

        boolean alreadyLinked = document.getAuthors()
                .stream()
                .anyMatch(existingAuthor -> existingAuthor.getId().equals(author.getId()));

        if (!alreadyLinked) {
            document.getAuthors().add(author);
        }
    }
}
