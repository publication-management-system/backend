package com.pms.publicationmanagement.dto.projects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateProjectDto {
    private String title;
    private String description;
}
