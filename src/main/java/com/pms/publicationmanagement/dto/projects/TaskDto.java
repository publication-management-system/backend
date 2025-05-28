package com.pms.publicationmanagement.dto.projects;

import com.pms.publicationmanagement.model.user.TaskState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TaskDto {
    private UUID id;
    private String title;
    private String description;
    private TaskState state;
}
