package com.pms.publicationmanagement.mapper;


import com.pms.publicationmanagement.dto.CitationDto;
import com.pms.publicationmanagement.model.profiling.Citation;

import java.util.ArrayList;
import java.util.List;

public class CitationDtoMapper {

    public static CitationDto toCitationDto (Citation citation) {
        CitationDto citationDto = new CitationDto();
        citationDto.id = citation.getId();
        citationDto.title = citation.getTitle();
        citationDto.link = citation.getLink();
        citationDto.document = citation.getDocument();
        return citationDto;
    }

    public static List<CitationDto> toCitationDtoList (List<Citation> citationList) {
        List<CitationDto> result = new ArrayList<>();
        for(Citation c : citationList) {
            result.add(toCitationDto(c));
        }
        return result;
    }
}
