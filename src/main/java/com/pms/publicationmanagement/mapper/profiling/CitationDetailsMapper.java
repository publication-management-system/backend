package com.pms.publicationmanagement.mapper.profiling;

import com.pms.publicationmanagement.dto.citations.CitationDetailsDto;
import com.pms.publicationmanagement.model.profiling.Citation;

import java.util.List;
import java.util.Objects;

public class CitationDetailsMapper {

    public static CitationDetailsDto toDetailsDto(Citation citation) {
        if (citation == null) {
            return null;
        }

        return new CitationDetailsDto(
                citation.getTitle(),
                citation.getLink(),
                citation.getPdf(),
                citation.getDocument() != null
                        ? citation.getDocument().getId()
                        : null
        );
    }

    public static List<CitationDetailsDto> toDetailsDtoList(List<Citation> citations) {
        return citations.stream()
                .map(CitationDetailsMapper::toDetailsDto)
                .filter(Objects::nonNull)
                .toList();
    }
}
