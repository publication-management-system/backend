package com.pms.publicationmanagement.dto.authors;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthorDetailsDto {
    private UUID id;
    private String name;
    private String firstName;
    private String lastName;
    private String middleName;
    private String googleScholarId;
    private String dblpId;
    private String wosId;
    private String institutionRole;
    private String institution;
    private String institutionMail;
    private String internalRefId;
    private String imageUrl;
    private String topics;
}
