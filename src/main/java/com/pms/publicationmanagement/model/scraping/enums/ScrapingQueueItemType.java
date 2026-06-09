package com.pms.publicationmanagement.model.scraping.enums;


import lombok.Getter;

@Getter
public enum ScrapingQueueItemType {
    DOCUMENT(1001L),
    DOCUMENT_DBLP(2001L),

    CITATIONS_GS(1002L),
    CITATIONS_DBLP(2002L),

    FIND_AUTHOR(7003L),
    FIND_AUTHOR_DBLP(8003L);

    private final Long priority;

    ScrapingQueueItemType(Long priority) {
        this.priority = priority;
    }
}
