package com.pms.publicationmanagement.service.scraping.dblp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DblpScrapingDto {
    private String firstName;
    private String lastName;
    private UUID sessionId;
}
