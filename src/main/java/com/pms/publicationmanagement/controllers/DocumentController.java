package com.pms.publicationmanagement.controllers;

import com.pms.publicationmanagement.dto.DocumentDto;
import com.pms.publicationmanagement.mapper.DocumentDtoMapper;
import com.pms.publicationmanagement.model.profiling.Author;
import com.pms.publicationmanagement.service.profiling.DocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping
    @Operation(security = {@SecurityRequirement(name = "SwaggerAuthentication")})
    public void addDocument(@RequestBody DocumentDto documentDto) {
        documentService.addDocument(documentDto.id, documentDto.title, documentDto.publicationDate, documentDto.authors,
                documentDto.issued, documentDto.volume, documentDto.issue, documentDto.pages, documentDto.publisher,
                documentDto.description, documentDto.citedIn, documentDto.link);
    }

    @GetMapping("by-title")
    @Operation(security = {@SecurityRequirement(name = "SwaggerAuthentication")})
    public List<DocumentDto> getDocumentByTitle(@RequestParam String title) {
        return DocumentDtoMapper.toDocumentDtoList(documentService.getByTitle(title));
    }

    @GetMapping("by-publication-date")
    @Operation(security = {@SecurityRequirement(name = "SwaggerAuthentication")})
    public List<DocumentDto> getDocumentByPublicationDate(@RequestParam String publicationDate) {
        return DocumentDtoMapper.toDocumentDtoList(documentService.getByPublicationDate(publicationDate));
    }

    @GetMapping("by-authors")
    @Operation(security = {@SecurityRequirement(name = "SwaggerAuthentication")})
    public List<DocumentDto> getDocumentByAuthors(List<Author> authors) {
        return DocumentDtoMapper.toDocumentDtoList(documentService.getByAuthors(authors));
    }

    @GetMapping("by-issued")
    @Operation(security = {@SecurityRequirement(name = "SwaggerAuthentication")})
    public List<DocumentDto> getDocumentByIssued(String issued) {
        return DocumentDtoMapper.toDocumentDtoList(documentService.getByIssued(issued));
    }

    @GetMapping("by-publisher")
    @Operation(security = {@SecurityRequirement(name = "SwaggerAuthentication")})
    public List<DocumentDto> getDocumentByPublisher(String publisher) {
        return DocumentDtoMapper.toDocumentDtoList(documentService.getByPublisher(publisher));
    }

    @GetMapping
    @Operation(security = {@SecurityRequirement(name = "SwaggerAuthentication")})
    public List<DocumentDto> getAllDocuments() {
        return DocumentDtoMapper.toDocumentDtoList(documentService.getAll());
    }

    @DeleteMapping("/{id}")
    @Operation(security = {@SecurityRequirement(name = "SwaggerAuthentication")})
    public void removeDocumentById(@PathVariable UUID id) {
        documentService.removeAuthor(id);
    }
}
