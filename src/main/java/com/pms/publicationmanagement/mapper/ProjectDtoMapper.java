package com.pms.publicationmanagement.mapper;

import com.pms.publicationmanagement.dto.user.ProjectDto;
import com.pms.publicationmanagement.model.user.Project;

import java.util.ArrayList;
import java.util.List;

public class ProjectDtoMapper {

    public static ProjectDto toProjectDto(Project Project){
        ProjectDto dto = new ProjectDto();
        dto.id = Project.getId();
        dto.title = Project.getTitle();
        dto.description = Project.getDescription();

        return dto;
    }

    public static List<ProjectDto> toProjectDtoList(List <Project> projectList) {
        List<ProjectDto> result = new ArrayList<>();

        for(Project p : projectList){
            result.add(toProjectDto(p));
        }
        return result;
    }
}
