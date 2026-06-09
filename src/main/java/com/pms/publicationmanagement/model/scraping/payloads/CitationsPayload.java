package com.pms.publicationmanagement.model.scraping.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CitationsPayload {
    private String title;
    private String citationsLink;
    private String pdfLink;
    private String refId;
}

