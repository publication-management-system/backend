package com.pms.publicationmanagement.service.scraping.transformation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.pms.publicationmanagement.model.profiling.Citation;
import com.pms.publicationmanagement.model.profiling.Document;
import com.pms.publicationmanagement.model.scraping.DataSourceType;
import com.pms.publicationmanagement.model.scraping.payloads.CitationsGsPayload;
import com.pms.publicationmanagement.model.scraping.queue.ScrapingQueueItem;
import com.pms.publicationmanagement.repository.CitationRepository;
import com.pms.publicationmanagement.service.scraping.dto.ScrapingResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CitationsTransformer implements ITransformer  {
    public static final Gson GSON = new GsonBuilder().disableHtmlEscaping().create();

    private final CitationRepository citationRepository;

    @Override
    public void save(ScrapingQueueItem scrapingRequest, ScrapingResponse scrapingResponse, DataSourceType providerType) {
        List<CitationsGsPayload> payloadCitations = new Gson().fromJson(scrapingResponse.getData(),
                new TypeToken<List<CitationsGsPayload>>() {}.getType());

        for (var payload : payloadCitations) {
            var existingCitations = citationRepository.findByTitle(payload.getCitationsLink());

            if (existingCitations == null || existingCitations.isEmpty()) {
                var newCitation = toCitation(payload, scrapingRequest.getRefId());
                citationRepository.save(newCitation);
            }
        }
    }

    private static Citation toCitation(CitationsGsPayload payload, String refId) {
        return Citation.builder()
                .id(UUID.randomUUID())
                .title(payload.getTitle())
                .link(payload.getCitationsLink())
                .title(payload.getTitle())
                .pdf(payload.getPdfLink())
                .document(Document.builder().id(UUID.fromString(refId)).build())
                .build();
    }
}
