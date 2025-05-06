package com.pms.publicationmanagement.dto.user;

import java.util.UUID;

public class ProjectDto {

    public UUID id;

    public String title;

    public String description;

    public ProjectDto() {
    }

    public ProjectDto(UUID id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }
}
