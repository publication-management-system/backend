package com.pms.publicationmanagement.dto.stats;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorStatistics {
    private List<DocumentByYear> documentsByYear;

    private List<CitationsByYear> citationsByYear;

    private Long totalNumberOfCitations;

    private List<String> topics;
}
