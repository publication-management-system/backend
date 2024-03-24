package com.pms.publicationmanagement.controllers;

import com.pms.publicationmanagement.dto.AuthorDto;
import com.pms.publicationmanagement.mapper.AuthorDtoMapper;
import com.pms.publicationmanagement.services.AuthorService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping
    public void addAuthor(@RequestBody AuthorDto authorDto) {
        authorService.addAuthor(authorDto.id, authorDto.name, authorDto.role,
                authorDto.institution, authorDto.institutionMail, authorDto.documents);

    }

    @GetMapping("by-name")
    public List<AuthorDto> getAuthorByName(@RequestParam String name) {
        return AuthorDtoMapper.toAuthorDtoList(authorService.getAuthorByName(name));
    }

    @GetMapping("by-role")
    public List<AuthorDto> getAuthorByRole(@RequestParam String role) {
        return AuthorDtoMapper.toAuthorDtoList(authorService.getAuthorByRole(role));
    }

    @GetMapping("by-institution")
    public List<AuthorDto> getAuthorByInstitution(@RequestParam String institution) {
        return AuthorDtoMapper.toAuthorDtoList(authorService.getAuthorByInstitution(institution));
    }

    @GetMapping("by-institution-mail")
    public AuthorDto getAuthorByInstitutionMail(@RequestParam String institutionMail) {
        return AuthorDtoMapper.toAuthorDto(authorService.getAuthorByInstitutionMail(institutionMail));
    }

    @DeleteMapping("/{id}")
    public void removeAuthorById(@PathVariable Integer id) {
        authorService.removeAuthor(id);
    }

    @GetMapping
    public List<AuthorDto> getAllAuthors() {
        return AuthorDtoMapper.toAuthorDtoList(authorService.getAll());
    }

}
