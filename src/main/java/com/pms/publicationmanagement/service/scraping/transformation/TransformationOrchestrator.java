package com.pms.publicationmanagement.service.scraping.transformation;

import com.pms.publicationmanagement.model.scraping.event.ScrapingEvent;
import com.pms.publicationmanagement.model.scraping.queue.ScrapingQueueItem;
import com.pms.publicationmanagement.model.scraping.enums.ScrapingQueueItemType;
import com.pms.publicationmanagement.repository.scraping.ScrapingEventRepository;
import com.pms.publicationmanagement.repository.scraping.ScrapingQueueItemsRepository;
import com.pms.publicationmanagement.service.scraping.dto.ScrapingResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransformationOrchestrator {

    private final AuthorTransformer authorTransformer;
    private final DocumentTransformer documentTransformer;
    private final CitationsTransformer citationsTransformer;
    private final ScrapingEventRepository scrapingEventRepository;

    public void transformScraping(ScrapingQueueItem scrapingRequest, ScrapingResponse scrapingResponse) {
        switch (ScrapingQueueItemType.valueOf(scrapingRequest.getType().name())) {
            case FIND_AUTHOR, FIND_AUTHOR_DBLP -> {
                log.info("Transforming author from response {}", scrapingResponse);
                authorTransformer.save(scrapingRequest, scrapingResponse, scrapingRequest.getProvider());
                log.info("Transformed author from response {}", scrapingResponse);
            }
            case DOCUMENT, DOCUMENT_DBLP -> {
                log.info("Transforming document from response {}", scrapingResponse);
                documentTransformer.save(scrapingRequest, scrapingResponse, scrapingRequest.getProvider());
                log.info("Transformed document from response {}", scrapingResponse);
            }
            case CITATIONS_GS -> {
                log.info("Transforming citations from response {}", scrapingResponse);
                citationsTransformer.save(scrapingRequest, scrapingResponse, scrapingRequest.getProvider());
                log.info("Transformed citations from response {}", scrapingResponse);
            }
            case CITATIONS_DBLP -> {
                log.info("Transforming citations from response {}", scrapingResponse);
                citationsTransformer.save(scrapingRequest, scrapingResponse, scrapingRequest.getProvider());
                log.info("Transformed citations from response {}", scrapingResponse);
            }
        }

        var scrapingEvent = ScrapingEvent.builder()
                .institutionId(scrapingRequest.getInstitutionId())
                .provider(scrapingRequest.getProvider())
                .type(scrapingRequest.getType())
                .scrapedAt(LocalDateTime.now())
                .success(true)
                .build();

        scrapingEventRepository.save(scrapingEvent);
    }

}
