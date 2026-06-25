package com.pms.publicationmanagement.service.scraping.transformation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.pms.publicationmanagement.model.profiling.Author;
import com.pms.publicationmanagement.model.profiling.Document;
import com.pms.publicationmanagement.model.scraping.enums.DataSourceType;
import com.pms.publicationmanagement.model.scraping.payloads.DocumentPayload;
import com.pms.publicationmanagement.model.scraping.queue.ScrapingQueueItem;
import com.pms.publicationmanagement.model.scraping.enums.ScrapingQueueItemType;
import com.pms.publicationmanagement.repository.AuthorRepository;
import com.pms.publicationmanagement.repository.DocumentRepository;
import com.pms.publicationmanagement.repository.scraping.ScrapingQueueItemsRepository;
import com.pms.publicationmanagement.service.scraping.dto.FindAuthorPayload;
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
    public static final Long THRESHOLD_LEVENSHTEIN_DOCUMENTS = 12L;
    public static final Long THRESHOLD_LEVENSHTEIN_DOCUMENTS_COAUTHORS = 4L;

    private final DocumentRepository documentRepository;
    private final AuthorRepository authorRepository;
    private final ScrapingQueueItemsRepository scrapingQueueItemsRepository;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public void save(
            ScrapingQueueItem scrapingRequest,
            ScrapingResponse scrapingResponse,
            DataSourceType providerType
    ) {
        DocumentPayload payload = null;
        try {
            payload = objectMapper.readValue(scrapingResponse.getData(), DocumentPayload.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

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

        var existingDocument = documentRepository
                .findExisting(payload.getProviderId(), payload.getTitle(), THRESHOLD_LEVENSHTEIN_DOCUMENTS)
                .orElse(null);

        Document document;

        if (existingDocument == null) {
            document = documentRepository.save(toDocument(payload, providerType));
        } else {
            updateExisting(existingDocument, payload, scrapingRequest.getProvider());
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

    private void updateExisting(Document document, DocumentPayload payload, DataSourceType provider) {
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

        if (document.getGoogleScholarId() == null && payload.getProviderId() != null && provider == DataSourceType.GOOGLE_SCHOLAR) {
            document.setGoogleScholarId(payload.getProviderId());
        }

        if (document.getDblpId() == null && payload.getProviderId() != null && provider == DataSourceType.DBLP) {
            document.setDblpId(payload.getProviderId());
        }

        if (document.getWosId() == null && payload.getProviderId() != null && provider == DataSourceType.WEB_OF_SCIENCE) {
            document.setWosId(payload.getProviderId());
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
            ScrapingQueueItem request,
            ScrapingResponse response,
            String internalDocumentId
    ) {
        var toSave = new ArrayList<ScrapingQueueItem>();

        for (var toEnqueue : response.getQueueItems()) {
            String payload = null;
            try {
                payload = objectMapper.writeValueAsString(new UrlPayload(toEnqueue.getLink()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            toSave.add(
                    ScrapingQueueItem.builder()
                            .type(ScrapingQueueItemType.valueOf(toEnqueue.getType()))
                            .payload(payload)
                            .refId(internalDocumentId)
                            .scrapingLink(toEnqueue.getLink())
                            .createdAt(LocalDateTime.now())
                            .createdById(request.getCreatedById())
                            .createdByName(request.getCreatedByName())
                            .institutionId(request.getInstitutionId())
                            .priority(ScrapingQueueItemType.valueOf(toEnqueue.getType()).getPriority())
                            .provider(request.getProvider())
                            .build()
            );
        }

        enqueueCoauthorsIfAny(request, response);

        scrapingQueueItemsRepository.saveAll(toSave);
    }

    private void enqueueCoauthorsIfAny(ScrapingQueueItem request, ScrapingResponse response) {
        try {
            DocumentPayload payload = null;
            try {
                payload = objectMapper.readValue(response.getData(), DocumentPayload.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            var coAuthorsToEnqueue = new ArrayList<ScrapingQueueItem>();
            for (var coAuthor : payload.getCoAuthorsNames()) {
                try {
                    String firstName = coAuthor.split(" ")[0];
                    String lastName = coAuthor.split(" ")[1];

                    ScrapingQueueItemType type = request.getProvider() == DataSourceType.DBLP
                            ? ScrapingQueueItemType.FIND_AUTHOR_DBLP
                            : ScrapingQueueItemType.FIND_AUTHOR;

                    var payloadJson = new Gson().toJson(new FindAuthorPayload(firstName, lastName));

                    boolean existsByName = !authorRepository.findExistingByName(firstName + " " + lastName,
                            THRESHOLD_LEVENSHTEIN_DOCUMENTS_COAUTHORS).isEmpty();

                    boolean alreadyEnqueued = scrapingQueueItemsRepository.findByPayload(payloadJson)
                            .size() > 0;

                    if (existsByName || alreadyEnqueued) {
                        log.info("Author already exists enqueued = {}, existsByName = {}", alreadyEnqueued, existsByName);
                        continue;
                    }

                    var enqueueCoAuthor = ScrapingQueueItem.builder()
                            .type(type)
                            .priority(type.getPriority())
                            .payload(payloadJson)
                            .createdById(request.getCreatedById())
                            .provider(request.getProvider())
                            .institutionId(request.getInstitutionId())
                            .createdByName(request.getCreatedByName())
                            .createdAt(LocalDateTime.now())
                            .refId(UUID.randomUUID().toString())
                            .build();

                    coAuthorsToEnqueue.add(enqueueCoAuthor);
                } catch (Exception ex) {
                    log.warn("Coauthor problem {}", coAuthor, ex);
                }
            }
            scrapingQueueItemsRepository.saveAll(coAuthorsToEnqueue);
        } catch (Exception ex) {
            log.error("COULD NOT SAVE AUTHOR", ex);
        }
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