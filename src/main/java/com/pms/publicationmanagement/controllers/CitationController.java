package com.pms.publicationmanagement.controllers;

import com.pms.publicationmanagement.dto.CitationDto;
import com.pms.publicationmanagement.mapper.CitationDtoMapper;
import com.pms.publicationmanagement.services.CitationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/citations")
public class CitationController {

    private final CitationService citationService;

    public CitationController(CitationService citationService) {
        this.citationService = citationService;
    }

    @PostMapping
    public void addCitation(@RequestBody CitationDto citationDto) {
        citationService.addCitation(citationDto.id, citationDto.title, citationDto.link, citationDto.document);
    }

    @DeleteMapping("/{id}")
    public void deleteCitation(@PathVariable Integer id) {
        citationService.deleteCitation(id);
    }

    @GetMapping
    public List<CitationDto> findAllCitations() {
        return CitationDtoMapper.toCitationDtoList(citationService.findAll());
    }
}
