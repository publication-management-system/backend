package com.pms.publicationmanagement.service.scraping.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ScrapingPayload {
    String payload;

    String refId;
}
