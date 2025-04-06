package com.pms.publicationmanagement.mapper;

import com.pms.publicationmanagement.dto.AuthorDto;
import com.pms.publicationmanagement.model.profiling.Author;

import java.util.ArrayList;
import java.util.List;

public class AuthorDtoMapper {

    public static AuthorDto toAuthorDto (Author author) {
        AuthorDto authorDto = new AuthorDto();
        authorDto.id = author.getId();
        authorDto.name = author.getName();
        authorDto.role = author.getRole();
        authorDto.institution = author.getInstitution();
        authorDto.institutionMail = author.getInstitutionMail();
        authorDto.documents = author.getDocuments();
        return authorDto;
    }

    public static List<AuthorDto> toAuthorDtoList(List<Author> authorList) {
        List<AuthorDto> result = new ArrayList<>();
        for(Author a : authorList) {
            result.add(toAuthorDto(a));
        }
        return result;
    }
}
