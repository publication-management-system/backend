package com.pms.publicationmanagement.service.scraping.googlescholar.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoogleScholarScrapingDto {
    private String firstName;
    private String lastName;
    private UUID sessionId;
}
