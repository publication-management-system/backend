package com.pms.publicationmanagement.mapper;

import com.pms.publicationmanagement.dto.UserDto;
import com.pms.publicationmanagement.dto.projects.ProjectDto;
import com.pms.publicationmanagement.model.user.Project;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProjectDtoMapper {

    public static ProjectDto toProjectDto(Project project){
        ProjectDto dto = new ProjectDto();
        dto.setId(project.getId());
        dto.setTitle(project.getTitle());
        dto.setDescription(project.getDescription());
        dto.setUserDtoList(UserDtoMapper.toUserDtoList(project.getUsers()));
        for (UserDto userDto : dto.getUserDtoList()) {
            if (userDto.id.equals(project.getProjectOwnerId())) {
                dto.setProjectOwner(userDto);
            }
        }
        if (project.getCreatedAt() != null) {
            dto.setCreatedAt(project.getCreatedAt().toString());
        }
        dto.setCreatedAt(LocalDateTime.of(2025, 1, 1, 0, 0, 0).toString());

        return dto;
    }

    public static List<ProjectDto> toProjectDtoList(List<Project> projectList) {
        List<ProjectDto> result = new ArrayList<>();

        for(Project p : projectList){
            result.add(toProjectDto(p));
        }

        return result;
    }
}
