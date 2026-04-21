package com.pms.publicationmanagement.model.scraping.queue;


import lombok.Getter;

@Getter
public enum ScrapingQueueItemType {
    DOCUMENT(1L),
    CITATION(2L),
    CITATIONS_GS(2L),
    FIND_AUTHOR(3L);

    private final Long priority;

    ScrapingQueueItemType(Long priority) {
        this.priority = priority;
    }
}
