package com.pms.publicationmanagement.dto.projects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTitleDescriptionDto {
    private String title;
    private String description;
}
