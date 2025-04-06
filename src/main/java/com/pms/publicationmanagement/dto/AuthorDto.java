package com.pms.publicationmanagement.dto;

import com.pms.publicationmanagement.model.profiling.Document;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
public class AuthorDto {
    public UUID id;

    public String name;

    public String role;

    public String institution;

    public String institutionMail;

    public List<Document> documents;
}
