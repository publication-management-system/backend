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
import com.pms.publicationmanagement.repository.DocumentRepository;
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
public class CitationsTransformer implements ITransformer {
    public static final Gson GSON = new GsonBuilder().disableHtmlEscaping().create();

    private final CitationRepository citationRepository;
    private final DocumentRepository documentRepository;

    @Override
    public void save(
            ScrapingQueueItem scrapingRequest,
            ScrapingResponse scrapingResponse,
            DataSourceType providerType
    ) {
        List<CitationsGsPayload> payloadCitations = GSON.fromJson(
                scrapingResponse.getData(),
                new TypeToken<List<CitationsGsPayload>>() {}.getType()
        );

        UUID documentId = UUID.fromString(scrapingResponse.getRefId());

        var document = documentRepository.findById(documentId)
                .orElseThrow(() -> new IllegalStateException("Document not found: " + documentId));

        for (var payload : payloadCitations) {
            boolean exists = citationRepository.existsByDocumentIdAndLink(
                    documentId,
                    payload.getCitationsLink()
            );

            if (!exists) {
                citationRepository.save(toCitation(payload, document));
            }
        }
    }

    private static Citation toCitation(CitationsGsPayload payload, Document document) {
        return Citation.builder()
                .id(UUID.randomUUID())
                .title(payload.getTitle())
                .link(payload.getCitationsLink())
                .pdf(payload.getPdfLink())
                .document(document)
                .build();
    }
}
