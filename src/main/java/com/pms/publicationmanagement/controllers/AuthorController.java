package com.pms.publicationmanagement.controllers;

import com.pms.publicationmanagement.dto.AuthorDto;
import com.pms.publicationmanagement.mapper.AuthorDtoMapper;
import com.pms.publicationmanagement.services.AuthorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
    @Operation(security = {@SecurityRequirement(name = "SwaggerAuthentication")})
    public void addAuthor(@RequestBody AuthorDto authorDto) {
        authorService.addAuthor(authorDto.id, authorDto.name, authorDto.role,
                authorDto.institution, authorDto.institutionMail, authorDto.documents);

    }

    @GetMapping("by-name")
    @Operation(security = {@SecurityRequirement(name = "SwaggerAuthentication")})
    public List<AuthorDto> getAuthorByName(@RequestParam String name) {
        return AuthorDtoMapper.toAuthorDtoList(authorService.getAuthorByName(name));
    }

    @GetMapping("by-role")
    @Operation(security = {@SecurityRequirement(name = "SwaggerAuthentication")})
    public List<AuthorDto> getAuthorByRole(@RequestParam String role) {
        return AuthorDtoMapper.toAuthorDtoList(authorService.getAuthorByRole(role));
    }

    @GetMapping("by-institution")
    @Operation(security = {@SecurityRequirement(name = "SwaggerAuthentication")})
    public List<AuthorDto> getAuthorByInstitution(@RequestParam String institution) {
        return AuthorDtoMapper.toAuthorDtoList(authorService.getAuthorByInstitution(institution));
    }

    @GetMapping("by-institution-mail")
    @Operation(security = {@SecurityRequirement(name = "SwaggerAuthentication")})
    public AuthorDto getAuthorByInstitutionMail(@RequestParam String institutionMail) {
        return AuthorDtoMapper.toAuthorDto(authorService.getAuthorByInstitutionMail(institutionMail));
    }

    @DeleteMapping("/{id}")
    @Operation(security = {@SecurityRequirement(name = "SwaggerAuthentication")})
    public void removeAuthorById(@PathVariable Integer id) {
        authorService.removeAuthor(id);
    }

    @GetMapping
    @Operation(security = {@SecurityRequirement(name = "SwaggerAuthentication")})
    public List<AuthorDto> getAllAuthors() {
        return AuthorDtoMapper.toAuthorDtoList(authorService.getAll());
    }

}
