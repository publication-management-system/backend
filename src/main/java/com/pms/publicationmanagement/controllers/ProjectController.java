package com.pms.publicationmanagement.controllers;

import com.pms.publicationmanagement.dto.projects.CreateProjectDto;
import com.pms.publicationmanagement.dto.projects.ProjectDto;
import com.pms.publicationmanagement.dto.projects.UpdateTitleDescriptionDto;
import com.pms.publicationmanagement.mapper.ProjectDtoMapper;
import com.pms.publicationmanagement.model.user.Project;
import com.pms.publicationmanagement.service.projects.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    @Operation(security = {@SecurityRequirement(name = "SwaggerAuthentication")})
    public ProjectDto addProject(@RequestBody CreateProjectDto projectDto, @RequestParam UUID userId) {
        return projectService.addProject(projectDto, userId);
    }

    @GetMapping("/{id}")
    @Operation(security = {@SecurityRequirement(name = "SwaggerAuthentication")})
    public ProjectDto getProjectById(@PathVariable UUID id) {
        return ProjectDtoMapper.toProjectDto(projectService.findById(id));
    }

    @GetMapping("/user/{userId}")
    @Operation(security = {@SecurityRequirement(name = "SwaggerAuthentication")})
    public Page<ProjectDto> getProjects(@PathVariable UUID userId,
                                        @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
                                        @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        Page<Project> allByUserId = projectService.findAllByUserId(userId, pageNumber, pageSize);
        return allByUserId.map(ProjectDtoMapper::toProjectDto);
    }

    @DeleteMapping("/{id}")
    @Operation(security = {@SecurityRequirement(name = "SwaggerAuthentication")})
    public void deleteProject(@PathVariable UUID id) {
        projectService.deleteProject(id);
    }

    @PatchMapping("/{id}/edit")
    @Operation(security = {@SecurityRequirement(name = "SwaggerAuthentication")})
    public ProjectDto updateTaskDescription(@PathVariable UUID id, @RequestBody UpdateTitleDescriptionDto updateTitleDescriptionDto) {
        return projectService.updateTitleAndDescription(id, updateTitleDescriptionDto);
    }
}
