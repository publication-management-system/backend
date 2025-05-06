package com.pms.publicationmanagement.dto.user;

import com.pms.publicationmanagement.model.user.TaskState;

import java.util.UUID;

public class TaskDto {

    public UUID id;

    public String title;

    public String description;
    public TaskState state;

    public TaskDto() {
    }

    public TaskDto(UUID id, String title, String description, TaskState state) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.state = state;
    }
}
