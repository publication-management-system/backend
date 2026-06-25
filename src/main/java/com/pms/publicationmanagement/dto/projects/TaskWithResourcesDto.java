package com.pms.publicationmanagement.dto.projects;

import com.pms.publicationmanagement.model.projects.TaskState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskWithResourcesDto {
    private UUID id;
    private String title;
    private String description;
    private UUID projectId;
    private TaskState state;
    private List<TaskResourceDto> taskResources;
}
