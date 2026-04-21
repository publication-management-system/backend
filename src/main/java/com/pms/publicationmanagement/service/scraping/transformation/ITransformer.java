package com.pms.publicationmanagement.service.scraping.transformation;

import com.pms.publicationmanagement.model.scraping.DataSourceType;
import com.pms.publicationmanagement.model.scraping.queue.ScrapingQueueItem;
import com.pms.publicationmanagement.service.scraping.dto.ScrapingResponse;

public interface ITransformer {

    void save(ScrapingQueueItem scrapingRequest, ScrapingResponse scrapingResponse, DataSourceType providerType);
}
