package com.pms.publicationmanagement.model.scraping.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorCitationsPayload {

    private String link;
    private String title;
    private String documentId;
}
