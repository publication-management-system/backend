package com.pms.publicationmanagement.controllers.publicendpoints;

import com.pms.publicationmanagement.dto.documents.DocumentDetailsDto;
import com.pms.publicationmanagement.service.profiling.DocumentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/public/documents")
public class PublicDocumentController {

    private final DocumentService documentService;

    @GetMapping()
    public DocumentDetailsDto getDocuments(@RequestParam("documentId") UUID documentId) {
        return documentService.findDocumentById(documentId);
    }
}
