package com.pms.publicationmanagement.dto.projects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTaskResourceDto {
    private String data;
    private String url;
    private String resourceName;
}
