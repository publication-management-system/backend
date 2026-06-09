package com.pms.publicationmanagement.service.scraping.transformation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pms.publicationmanagement.model.profiling.Author;
import com.pms.publicationmanagement.model.scraping.enums.DataSourceType;
import com.pms.publicationmanagement.model.scraping.payloads.AuthorProfilePayload;
import com.pms.publicationmanagement.model.scraping.queue.ScrapingQueueItem;
import com.pms.publicationmanagement.model.scraping.enums.ScrapingQueueItemType;
import com.pms.publicationmanagement.repository.AuthorRepository;
import com.pms.publicationmanagement.repository.scraping.ScrapingQueueItemsRepository;
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
public class AuthorTransformer implements ITransformer {

    public static final Long TRESHOLD_DISTANCE_LEVENSHTEIN = 8L;

    private final AuthorRepository authorRepository;
    private final ScrapingQueueItemsRepository scrapingQueueItemsRepository;
    private final ObjectMapper objectMapper;

    public void save(ScrapingQueueItem scrapingRequest, ScrapingResponse scrapingResponse, DataSourceType providerType) {
        AuthorProfilePayload payload = null;

        try {
            payload = objectMapper.readValue(scrapingResponse.getData(), AuthorProfilePayload.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        var existingAuthor = authorRepository.findExisting(payload.getProviderId(), payload.getAuthorName(),
                        TRESHOLD_DISTANCE_LEVENSHTEIN)
                .orElse(null);

        Author author;

        if (existingAuthor == null) {
            author = authorRepository.save(toAuthor(payload, providerType));
        } else {
            updateExisting(existingAuthor, payload, scrapingRequest.getProvider());
            author = authorRepository.save(existingAuthor);
        }

        enqueueNextItems(scrapingRequest, scrapingResponse, author.getId().toString());
    }

    private static Author toAuthor(AuthorProfilePayload profile, DataSourceType providerType) {
        return Author.builder()
                .id(UUID.randomUUID())
                .name(profile.getAuthorName())
                .firstName(profile.getFirstName())
                .middleName(profile.getMiddleName())
                .lastName(profile.getLastName())
                .imageUrl(profile.getImageUrl())
                .institution(profile.getInstitution())
                .topics(String.join(",", profile.getTopicElements()))
                .googleScholarId(DataSourceType.GOOGLE_SCHOLAR == providerType ? profile.getProviderId() : null)
                .dblpId(DataSourceType.DBLP == providerType ? profile.getProviderId() : null)
                .wosId(DataSourceType.WEB_OF_SCIENCE == providerType ? profile.getProviderId() : null)
                .hIndex(profile.getH_index())
                .i10Index(profile.getI10_index())
                .build();
    }

    private void updateExisting(Author author, AuthorProfilePayload profile, DataSourceType provider) {
        if (author.getFirstName() == null && profile.getFirstName() != null) {
            author.setFirstName(profile.getFirstName());
        }

        if (author.getMiddleName() == null && profile.getMiddleName() != null) {
            author.setMiddleName(profile.getMiddleName());
        }

        if (author.getLastName() == null && profile.getLastName() != null) {
            author.setLastName(profile.getLastName());
        }

        if (author.getGoogleScholarId() == null && profile.getProviderId() != null && provider == DataSourceType.GOOGLE_SCHOLAR) {
            author.setGoogleScholarId(profile.getProviderId());
        }

        if (author.getDblpId() == null && profile.getProviderId() != null && provider == DataSourceType.DBLP) {
            author.setDblpId(profile.getProviderId());
        }

        if (author.getWosId() == null && profile.getProviderId() != null && provider == DataSourceType.WEB_OF_SCIENCE) {
            author.setWosId(profile.getProviderId());
        }

        if (author.getInstitutionRole() == null && profile.getInstitutionRole() != null) {
            author.setInstitutionRole(profile.getInstitutionRole());
        }

        if (author.getInstitution() == null && profile.getInstitution() != null) {
            author.setInstitution(profile.getInstitution());
        }

        if (author.getInstitutionMail() == null && profile.getEmail() != null) {
            author.setInstitutionMail(profile.getEmail());
        }

        if (author.getInternalRefId() == null && profile.getInternalRefId() != null) {
            author.setInternalRefId(profile.getInternalRefId());
        }

        if (author.getImageUrl() == null && profile.getImageUrl() != null) {
            author.setImageUrl(profile.getImageUrl());
        }

        if ((author.getTopics() == null || author.getTopics().isBlank()) && profile.getTopicElements() != null) {
            author.setTopics(String.join(",", profile.getTopicElements()));
        }
    }

    private void enqueueNextItems(ScrapingQueueItem item,
                                  ScrapingResponse response,
                                  String internalRefId) {
        var toSave = new ArrayList<ScrapingQueueItem>();

        for (var toEnqueue : response.getQueueItems()) {
            String payload = null;
            try {
                payload = objectMapper.writeValueAsString(new UrlPayload(toEnqueue.getLink()));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            toSave.add(
                    ScrapingQueueItem.builder()
                            .type(ScrapingQueueItemType.valueOf(toEnqueue.getType()))
                            .payload(payload)
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
