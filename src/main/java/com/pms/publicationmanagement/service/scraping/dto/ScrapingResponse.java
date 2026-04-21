package com.pms.publicationmanagement.service.scraping.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScrapingResponse {
    private String data;
    private List<ScrapingResponseQueueItem> queueItems;
    private String refId;
}
