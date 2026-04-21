package com.pms.publicationmanagement.service.scraping.transformation;

import com.pms.publicationmanagement.model.scraping.queue.ScrapingQueueItem;
import com.pms.publicationmanagement.model.scraping.queue.ScrapingQueueItemType;
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

    public void transformScraping(ScrapingQueueItem scrapingRequest, ScrapingResponse scrapingResponse) {
        if (ScrapingQueueItemType.FIND_AUTHOR == ScrapingQueueItemType.valueOf(scrapingRequest.getType().name())) {
            log.info("Transforming author from response {}", scrapingResponse);
            authorTransformer.save(scrapingRequest, scrapingResponse, scrapingRequest.getProvider());
            log.info("Transformed author from response {}", scrapingResponse);
            return;
        }

        if (ScrapingQueueItemType.DOCUMENT == ScrapingQueueItemType.valueOf(scrapingRequest.getType().name())) {
            log.info("Transforming document from response {}", scrapingResponse);
            documentTransformer.save(scrapingRequest, scrapingResponse, scrapingRequest.getProvider());
            log.info("Transformed document from response {}", scrapingResponse);
            return;
        }
    }
}
