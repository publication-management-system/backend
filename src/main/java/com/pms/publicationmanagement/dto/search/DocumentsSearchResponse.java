package com.pms.publicationmanagement.dto.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentsSearchResponse {
    private UUID id;
    private String title;
    private String publisher;
    private String volume;
}

