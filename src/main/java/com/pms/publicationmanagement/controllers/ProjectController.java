package com.pms.publicationmanagement.controllers;

import com.pms.publicationmanagement.dto.user.ProjectDto;
import com.pms.publicationmanagement.mapper.ProjectDtoMapper;
import com.pms.publicationmanagement.service.user.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/project")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController (ProjectService projectService) {this.projectService = projectService;}

    @PostMapping
    @Operation(security = {@SecurityRequirement(name = "SwaggerAuthentication")})
    public void addProject(@RequestBody ProjectDto projectDto) {
        projectService.AddProject(projectDto.title, projectDto.description);
    }

    @GetMapping("/{id}")
    @Operation(security = {@SecurityRequirement(name = "SwaggerAuthentication")})
    public void getTask(@PathVariable UUID id) {
        projectService.findById(id);
    }

    @GetMapping()
    @Operation(security = {@SecurityRequirement(name = "SwaggerAuthentication")})
    public List<ProjectDto> getTask() {
        return ProjectDtoMapper.toProjectDtoList(projectService.findAll());
    }

    @DeleteMapping("/{id}")
    @Operation(security = {@SecurityRequirement(name = "SwaggerAuthentication")})
    public void deleteProject(@PathVariable UUID id) {
        projectService.deleteProject(id);
    }

    @PatchMapping("/{id}/title")
    @Operation(security = {@SecurityRequirement(name = "SwaggerAuthentication")})
    public ProjectDto updateProjectTitle(@PathVariable UUID id, @RequestBody ProjectDto projectDto) {
        return projectService.updateProjectTitleById(id, projectDto);
    }

    @PatchMapping("/{id}/description")
    @Operation(security = {@SecurityRequirement(name = "SwaggerAuthentication")})
    public ProjectDto updateTaskDescription(@PathVariable UUID id, @RequestBody ProjectDto projectDto) {
        return projectService.updateProjectDescriptionById(id, projectDto);
    }
}
