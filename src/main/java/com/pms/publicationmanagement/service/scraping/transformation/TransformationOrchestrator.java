package com.pms.publicationmanagement.service.scraping.transformation;

import com.pms.publicationmanagement.model.scraping.queue.ScrapingQueueItem;
import com.pms.publicationmanagement.model.scraping.queue.ScrapingQueueItemType;
import com.pms.publicationmanagement.repository.ScrapingQueueItemsRepository;
import com.pms.publicationmanagement.service.scraping.dto.ScrapingResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransformationOrchestrator {

    private final AuthorTransformer authorTransformer;
    private final DocumentTransformer documentTransformer;
    private final CitationsTransformer citationsTransformer;
    private final ScrapingQueueItemsRepository scrapingQueueItemsRepository;

    public void transformScraping(ScrapingQueueItem scrapingRequest, ScrapingResponse scrapingResponse) {
        switch (ScrapingQueueItemType.valueOf(scrapingRequest.getType().name())) {
            case FIND_AUTHOR -> {
                log.info("Transforming author from response {}", scrapingResponse);
                authorTransformer.save(scrapingRequest, scrapingResponse, scrapingRequest.getProvider());
                log.info("Transformed author from response {}", scrapingResponse);
            }
            case DOCUMENT -> {
                log.info("Transforming document from response {}", scrapingResponse);
                documentTransformer.save(scrapingRequest, scrapingResponse, scrapingRequest.getProvider());
                log.info("Transformed document from response {}", scrapingResponse);
            }
            case CITATIONS_GS -> {
                log.info("Transforming citations from response {}", scrapingResponse);
                citationsTransformer.save(scrapingRequest, scrapingResponse, scrapingRequest.getProvider());
                log.info("Transformed citations from response {}", scrapingResponse);
            }
        }

        scrapingQueueItemsRepository.deleteById(scrapingRequest.getId());
    }

}
