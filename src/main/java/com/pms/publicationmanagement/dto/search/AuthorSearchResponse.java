package com.pms.publicationmanagement.dto.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorSearchResponse {
    private UUID id;
    private String name;
    private String institution;
    private String imageUrl;
}