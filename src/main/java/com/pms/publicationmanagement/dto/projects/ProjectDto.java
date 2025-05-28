package com.pms.publicationmanagement.dto.projects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProjectDto {
    private UUID id;
    private String title;
    private String description;
}
