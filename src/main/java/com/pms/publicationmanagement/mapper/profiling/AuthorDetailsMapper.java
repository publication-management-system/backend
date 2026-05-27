package com.pms.publicationmanagement.mapper.profiling;

import com.pms.publicationmanagement.dto.authors.AuthorDetailsDto;
import com.pms.publicationmanagement.model.profiling.Author;

import java.util.Objects;

public class AuthorDetailsMapper {

    public static AuthorDetailsDto toAuthorDetails(Author author) {
        if (Objects.isNull(author)) {
            return null;
        }

        AuthorDetailsDto dto = new AuthorDetailsDto();
        dto.setId(author.getId());
        dto.setName(author.getName());
        dto.setFirstName(author.getFirstName());
        dto.setLastName(author.getLastName());
        dto.setMiddleName(author.getMiddleName());
        dto.setGoogleScholarId(author.getGoogleScholarId());
        dto.setDblpId(author.getDblpId());
        dto.setWosId(author.getWosId());
        dto.setInstitutionRole(author.getInstitutionRole());
        dto.setInstitution(author.getInstitution());
        dto.setInstitutionMail(author.getInstitutionMail());
        dto.setInternalRefId(author.getInternalRefId());
        dto.setImageUrl(author.getImageUrl());
        dto.setTopics(author.getTopics());

        return dto;
    }
}
