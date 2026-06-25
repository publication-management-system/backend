package com.pms.publicationmanagement.dto.projects;

import com.pms.publicationmanagement.model.projects.TaskResourceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskResourceDto {
    private UUID id;
    private String resourceName;
    private TaskResourceType resourceType;
    private String data;
    private String url;
}
