package com.pms.publicationmanagement.dto.projects;

import com.pms.publicationmanagement.model.projects.TaskResourceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTaskResourceDto {
    private String resourceName;
    private TaskResourceType resourceType;
    private String data;
    private String url;
}
