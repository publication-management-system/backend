package com.pms.publicationmanagement.dto.projects;

import com.pms.publicationmanagement.model.user.TaskState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTaskDto {
    private String title;
    private String description;
}
