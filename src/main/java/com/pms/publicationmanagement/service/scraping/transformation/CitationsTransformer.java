package com.pms.publicationmanagement.service.scraping.transformation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pms.publicationmanagement.model.profiling.Citation;
import com.pms.publicationmanagement.model.profiling.Document;
import com.pms.publicationmanagement.model.scraping.enums.DataSourceType;
import com.pms.publicationmanagement.model.scraping.payloads.CitationsPayload;
import com.pms.publicationmanagement.model.scraping.queue.ScrapingQueueItem;
import com.pms.publicationmanagement.repository.CitationRepository;
import com.pms.publicationmanagement.repository.DocumentRepository;
import com.pms.publicationmanagement.service.scraping.dto.ScrapingResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CitationsTransformer implements ITransformer {

    private final CitationRepository citationRepository;
    private final DocumentRepository documentRepository;
    private final ObjectMapper objectMapper;

    @Override
    public void save(ScrapingQueueItem scrapingRequest, ScrapingResponse scrapingResponse, DataSourceType providerType) {
        List<CitationsPayload> payloadCitations = null;
        try {
            payloadCitations = objectMapper.readValue(
                    scrapingResponse.getData(),
                    new TypeReference<>() {}
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

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

    private static Citation toCitation(CitationsPayload payload, Document document) {
        return Citation.builder()
                .id(UUID.randomUUID())
                .title(payload.getTitle())
                .link(payload.getCitationsLink())
                .pdf(payload.getPdfLink())
                .document(document)
                .build();
    }
}
