package com.pms.publicationmanagement.dto.citations;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CitationDetailsDto {
    private String title;
    private String link;
    private String pdf;
    private UUID documentId;
}
