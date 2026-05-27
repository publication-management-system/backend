package com.pms.publicationmanagement.service.scraping.transformation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pms.publicationmanagement.model.profiling.Author;
import com.pms.publicationmanagement.model.scraping.DataSourceType;
import com.pms.publicationmanagement.model.scraping.payloads.AuthorProfilePayload;
import com.pms.publicationmanagement.model.scraping.queue.ScrapingQueueItem;
import com.pms.publicationmanagement.model.scraping.queue.ScrapingQueueItemType;
import com.pms.publicationmanagement.repository.AuthorRepository;
import com.pms.publicationmanagement.repository.scraping.ScrapingEventRepository;
import com.pms.publicationmanagement.repository.scraping.ScrapingQueueItemsRepository;
import com.pms.publicationmanagement.service.scraping.dto.ScrapingResponse;
import com.pms.publicationmanagement.service.scraping.dto.UrlPayload;
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
public class AuthorTransformer implements ITransformer {

    public static final Gson GSON = new GsonBuilder().disableHtmlEscaping().create();
    private final AuthorRepository authorRepository;
    private final ScrapingQueueItemsRepository scrapingQueueItemsRepository;

    public void save(ScrapingQueueItem scrapingRequest, ScrapingResponse scrapingResponse, DataSourceType providerType) {
        var payload = GSON.fromJson(scrapingResponse.getData(), AuthorProfilePayload.class);

        var existingAuthor = findExistingAuthor(providerType, payload.getProviderId()).orElse(null);

        Author author;

        if (existingAuthor == null) {
            author = authorRepository.save(toAuthor(payload, providerType));
        } else {
            updateExisting(existingAuthor, payload);
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
                .h_index(profile.getH_index())
                .i10_index(profile.getI10_index())
                .build();
    }

    private void updateExisting(Author author, AuthorProfilePayload profile) {
        if (author.getFirstName() == null && profile.getFirstName() != null) {
            author.setFirstName(profile.getFirstName());
        }

        if (author.getMiddleName() == null && profile.getMiddleName() != null) {
            author.setMiddleName(profile.getMiddleName());
        }

        if (author.getLastName() == null && profile.getLastName() != null) {
            author.setLastName(profile.getLastName());
        }

        if (author.getInstitution() == null && profile.getInstitution() != null) {
            author.setInstitution(profile.getInstitution());
        }

        if (author.getInstitutionMail() == null && profile.getEmail() != null) {
            author.setInstitutionMail(profile.getEmail());
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

    public Optional<Author> findExistingAuthor(DataSourceType providerType, String providerId) {
        return switch (providerType) {
            case GOOGLE_SCHOLAR -> authorRepository.findByGoogleScholarId(providerId);
            case DBLP -> authorRepository.findByDblpId(providerId);
            case WEB_OF_SCIENCE -> authorRepository.findByWosId(providerId);
            default -> throw new RuntimeException("Invalid provider");
        };
    }
}
