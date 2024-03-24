package com.pms.publicationmanagement.controllers;

import com.pms.publicationmanagement.dto.DocumentDto;
import com.pms.publicationmanagement.mapper.DocumentDtoMapper;
import com.pms.publicationmanagement.model.Author;
import com.pms.publicationmanagement.services.DocumentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/documents")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping
    public void addDocument(@RequestBody DocumentDto documentDto) {
        documentService.addDocument(documentDto.id, documentDto.title, documentDto.publicationDate, documentDto.authors,
                documentDto.issued, documentDto.volume, documentDto.issue, documentDto.pages, documentDto.publisher,
                documentDto.description, documentDto.citedIn);
    }

    @GetMapping("by-title")
    public List<DocumentDto> getDocumentByTitle(@RequestParam String title) {
        return DocumentDtoMapper.toDocumentDtoList(documentService.getByTitle(title));
    }

    @GetMapping("by-publication-date")
    public List<DocumentDto> getDocumentByPublicationDate(@RequestParam String publicationDate) {
        return DocumentDtoMapper.toDocumentDtoList(documentService.getByPublicationDate(publicationDate));
    }

    @GetMapping("by-authors")
    public List<DocumentDto> getDocumentByAuthors(List<Author> authors) {
        return DocumentDtoMapper.toDocumentDtoList(documentService.getByAuthors(authors));
    }

    @GetMapping("by-issued")
    public List<DocumentDto> getDocumentByIssued(String issued) {
        return DocumentDtoMapper.toDocumentDtoList(documentService.getByIssued(issued));
    }

    @GetMapping("by-publisher")
    public List<DocumentDto> getDocumentByPublisher(String publisher) {
        return DocumentDtoMapper.toDocumentDtoList(documentService.getByPublisher(publisher));
    }

    @GetMapping
    public List<DocumentDto> getAllDocuments() {
        return DocumentDtoMapper.toDocumentDtoList(documentService.getAll());
    }

    @DeleteMapping("/{id}")
    public void removeDocumentById(@PathVariable Integer id) {
        documentService.removeAuthor(id);
    }
}
