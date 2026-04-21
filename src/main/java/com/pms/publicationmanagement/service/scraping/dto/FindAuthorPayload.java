package com.pms.publicationmanagement.service.scraping.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FindAuthorPayload {
    private String firstName;
    private String lastName;
}
