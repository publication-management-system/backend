package com.pms.publicationmanagement.controllers.publicendpoints;

import com.pms.publicationmanagement.dto.citations.CitationDetailsDto;
import com.pms.publicationmanagement.repository.CitationRepository;
import com.pms.publicationmanagement.service.profiling.CitationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/public/citations")
public class PublicCitationsController {

    private final CitationService citationService;

    @GetMapping
    public List<CitationDetailsDto> getByDocumentId(@RequestParam UUID documentId) {
        return citationService.findAllByDocumentId(documentId);
    }
}
