package com.pms.publicationmanagement.model.scraping.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//"author_name": "string",
//        "institution": "string",
//        "institutionRole": "string",
//        "email": "string",
//        "imageUrl": "string"

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
}
