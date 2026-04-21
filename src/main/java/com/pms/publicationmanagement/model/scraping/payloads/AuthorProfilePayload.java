package com.pms.publicationmanagement.model.scraping.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorProfilePayload {
    private String authorName;
    private String firstName;
    private String middleName;
    private String lastName;
    private String institution;
    private String institutionRole;
    private String email;
    private String imageUrl;
    private String providerId;
    private String internalRefId;
    private List<String> topicElements;
}
