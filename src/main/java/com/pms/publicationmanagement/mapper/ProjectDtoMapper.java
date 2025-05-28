package com.pms.publicationmanagement.mapper;

import com.pms.publicationmanagement.dto.projects.ProjectDto;
import com.pms.publicationmanagement.model.user.Project;

import java.util.ArrayList;
import java.util.List;

public class ProjectDtoMapper {

    public static ProjectDto toProjectDto(Project Project){
        ProjectDto dto = new ProjectDto();
        dto.setId(Project.getId());
        dto.setTitle(Project.getTitle());
        dto.setDescription(Project.getDescription());

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
